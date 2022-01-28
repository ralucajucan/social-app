package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.message.dto.ContactDTO;
import org.utcn.socialapp.message.dto.DraftDTO;
import org.utcn.socialapp.message.dto.MessageDTO;
import org.utcn.socialapp.message.dto.SendDTO;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_REQUEST;
import static org.utcn.socialapp.message.MessageStatus.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final static int MESSAGE_COUNT = 10;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageChannel clientOutboundChannel;

    public List<String> getConnectedUserList() {
        return simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());
    }

    public List<ContactDTO> getUserList(boolean withNewMessages) {
        if (withNewMessages) {
            return userRepository.findAllActive().stream().map(user -> new ContactDTO(
                            user.getEmail(),
                            user.getProfile().getFirstName() + " " + user.getProfile().getLastName(),
                            messageRepository.countSenderExcludeStatus(user, READ)
                    )
            ).collect(Collectors.toList());
        }
        return userRepository.findAllActive().stream().map(user -> new ContactDTO(
                        user.getEmail(),
                        user.getProfile().getFirstName() + " " + user.getProfile().getLastName()
                )
        ).collect(Collectors.toList());
    }

    public void sendUserList(boolean withNewMessages) {
        List<ContactDTO> usersList = getUserList(withNewMessages);
        List<String> connectedUsersList = getConnectedUserList();
        usersList.forEach(user -> user.setOnline(
                connectedUsersList.stream().anyMatch(user.getEmail()::equals)
        ));
        connectedUsersList.forEach(
                user -> simpMessagingTemplate.convertAndSendToUser(
                        user,
                        "/queue/list",
                        usersList)
        );
    }

    private MessageDTO saveMessage(String principalEmail, String userEmail, String text, String attachmentIds,
                                   MessageStatus status) throws BusinessException {
        if (!StringUtils.hasLength(principalEmail) || Stream.of(userEmail).anyMatch(Objects::isNull)
                || Stream.of(userEmail).anyMatch(s -> !StringUtils.hasLength(s))) {
            throw new BusinessException(BAD_REQUEST);
        }
        User principal = userRepository.findByEmail(principalEmail);
        User user = userRepository.findByEmail(userEmail);
        Long messageId = messageRepository.countAllBySenderAndReceiver(principal, user);
        Message message = new Message(
                messageId,
                principal,
                user,
                text,
                attachmentIds,
                status
        );
        try {
            message = messageRepository.save(message);
            if (status == SENT) {
                message = messageRepository.save(message);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new MessageDTO(message);
    }

    public MessageDTO saveDraft(String principalEmail, DraftDTO draftDTO) throws BusinessException {
        Message message = messageRepository.findByStatus(DRAFT);
        if (Objects.isNull(message)) {
            return saveMessage(
                    principalEmail,
                    draftDTO.getUser(),
                    draftDTO.getText(),
                    draftDTO.getAttachmentId(),
                    DRAFT
            );
        }
        message.setText(draftDTO.getText());
        message.setAttachmentIds(message.getAttachmentIds().concat("," + draftDTO.getAttachmentId()));
        messageRepository.save(message);
        return new MessageDTO(message);
    }

    @Transactional
    public void sendToUser(String principalEmail, SendDTO sendDTO) throws BusinessException {
        MessageDTO messageDTO = saveMessage(
                principalEmail,
                sendDTO.getUser(),
                sendDTO.getText(),
                sendDTO.getAttachmentIds(),
                SENT
        );
        simpMessagingTemplate.convertAndSendToUser(
                messageDTO.getReceiver(),
                "/queue/conv",
                messageDTO);
        if (!messageDTO.getReceiver().equals(messageDTO.getSender())) {
            simpMessagingTemplate.convertAndSendToUser(
                    messageDTO.getSender(),
                    "/queue/conv",
                    messageDTO);
        }
    }

    public List<MessageDTO> getConversation(String userEmail, int page) throws BusinessException {
        if (Objects.isNull(userEmail) || page < 0) {
            throw new BusinessException(BAD_REQUEST);
        }
        User user = userRepository.findByEmail(userEmail);
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, MESSAGE_COUNT, Sort.by("audit.createdOn").descending());

        List<Message> messages = messageRepository.findConversation(user, principal, pageable);
        messageRepository.updateReceivedAsRead(user);
        return messageRepository
                .findConversation(user, principal, pageable)
                .stream()
                .map(message -> {
                    if (message.getStatus() == RECEIVED) {
                        message.setStatus(READ);
                        messageRepository.save(message);
                        message.setStatus(RECEIVED);
                    }
                    return message;
                })
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }

    public void sendError(String sessionId, Exception e) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setMessage(e.getMessage());
        headerAccessor.setSessionId(sessionId);
        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }

    public void updateSentAsReceived(String receiverEmail) {
        User receiver = userRepository.findByEmail(receiverEmail);
        messageRepository.updateSentAsReceived(receiver);
    }
}
