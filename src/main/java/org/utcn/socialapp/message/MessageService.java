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

    public void sendToUser(String email, MessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSendToUser(
                email,
                "/queue/conv",
                messageDTO);
    }

    public List<String> getConnectedUserList() {
        return simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());
    }

    public List<ContactDTO> getUserList() {
        return userRepository.findAllActive().stream().map(user -> new ContactDTO(
                        user.getEmail(),
                        user.getProfile().getFirstName() + " " + user.getProfile().getLastName()
                )
        ).collect(Collectors.toList());
    }

    public void sendUserList(String principalEmail, boolean withNewMessages) {
        List<ContactDTO> usersList = getUserList();
        List<String> connectedUsersList = getConnectedUserList();
        usersList.forEach(user -> user.setOnline(
                connectedUsersList.stream().anyMatch(user.getEmail()::equals)
        ));
        if (withNewMessages) {
            List<ContactDTO> usersWithCountList = usersList.stream().map(user -> new ContactDTO(
                    user.getEmail(),
                    user.getName(),
                    user.isOnline(),
                    messageRepository.countReceivedWithSenderAndReceiver(principalEmail, user.getEmail())
                    )).collect(Collectors.toList());
            connectedUsersList.forEach(
                    user -> simpMessagingTemplate.convertAndSendToUser(
                            user,
                            "/queue/list",
                            user.equals(principalEmail)? usersWithCountList:usersList)
            );
        } else {
            connectedUsersList.forEach(
                    user -> simpMessagingTemplate.convertAndSendToUser(
                            user,
                            "/queue/list",
                            usersList)
            );
        }

    }

    public void removeAttachmentId(String attachmentId) throws BusinessException {
        Message message = messageRepository.findByAttachmentIdsContains(attachmentId);
        if (Objects.isNull(message)) {
            throw new BusinessException(BAD_REQUEST);
        }
        String attachmentIds = message.getAttachmentIds();
        int idx = attachmentIds.indexOf(attachmentId);
        if (idx != 0 && attachmentIds.charAt(idx - 1) == ',') attachmentId = "," + attachmentId;
        if (idx == 0 && !attachmentIds.equals(attachmentId)) attachmentId = attachmentId + ",";
        message.setAttachmentIds(attachmentIds.replace(attachmentId, ""));
        message.setEdited(true);
        try {
            messageRepository.save(message);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    private MessageDTO saveNewMessage(String principalEmail, String userEmail, String text, String attachmentIds,
                                      MessageStatus status) throws BusinessException {
        if (Stream.of(principalEmail, userEmail).anyMatch(Objects::isNull)
                || Stream.of(principalEmail, userEmail).anyMatch(s -> !StringUtils.hasLength(s))) {
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
            if (status == SENT && this.getConnectedUserList().contains(userEmail)) {
                message.setStatus(RECEIVED);
            }
            message = messageRepository.save(message);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new MessageDTO(message, principalEmail, userEmail);
    }

    public MessageDTO saveDraft(String principalEmail, String toEmail, String text,
                                String attachmentId) throws BusinessException {
        Message message = messageRepository.findDraft(principalEmail, toEmail);
        if (Objects.isNull(message)) {
            return saveNewMessage(
                    principalEmail,
                    toEmail,
                    text,
                    attachmentId,
                    DRAFT
            );
        }
        if (StringUtils.hasLength(text)) {
            message.setText(text);
        }
        if (StringUtils.hasLength(attachmentId)) {
            String currentAttachments = message.getAttachmentIds();
            if (StringUtils.hasLength(currentAttachments)) {
                message.setAttachmentIds(currentAttachments.concat("," + attachmentId));
            } else {
                message.setAttachmentIds(attachmentId);
            }
        }
        message = messageRepository.save(message);
        return new MessageDTO(message, principalEmail, toEmail);
    }

    public MessageDTO saveEdit(Long id, String principalEmail, SendDTO sendDTO) throws BusinessException {
        Message message = this.getMessage(id, principalEmail, sendDTO.getUser());
        message.setText(sendDTO.getText());
        message.setEdited(true);
        message = messageRepository.save(message);
        return new MessageDTO(message, principalEmail, sendDTO.getUser());
    }

    @Transactional
    public MessageDTO newSendMessage(String principalEmail, SendDTO sendDTO) throws BusinessException {
        Message message = messageRepository.findDraft(principalEmail, sendDTO.getUser());
        if (!Objects.isNull(message)) {
            message.setStatus(SENT);
            message.setText(sendDTO.getText());
            messageRepository.save(message);
        }

        return Objects.nonNull(message) ? new MessageDTO(message, principalEmail, sendDTO.getUser()) : saveNewMessage(
                principalEmail,
                sendDTO.getUser(),
                sendDTO.getText(),
                "",
                SENT
        );
    }

    public List<MessageDTO> getConversation(String userEmail, int page) throws BusinessException {
        if (Objects.isNull(userEmail) || page < 0) {
            throw new BusinessException(BAD_REQUEST);
        }
        User user = userRepository.findByEmail(userEmail);
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, MESSAGE_COUNT, Sort.by("audit.createdOn").descending());

        List<MessageDTO> messageDTOS = messageRepository.findConversation(principal, user, pageable);
        messageRepository.updateReceivedAsRead(principal, user);
        Message draft = messageRepository.findDraft(principal.getEmail(), userEmail);
        if (Objects.nonNull(draft)) {
            messageDTOS.add(new MessageDTO(draft, principal.getEmail(), userEmail));
        }
        return messageDTOS;
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

    public Message getMessage(Long id, String principalEmail, String contactEmail) throws BusinessException {
        Message message = messageRepository.findByIdAndEmails(id, principalEmail, contactEmail);
        if (Objects.isNull(message)) {
            throw new BusinessException(BAD_REQUEST);
        }
        return message;
    }

    public MessageDTO softDelete(Message message, String principalEmail, String contactEmail) throws BusinessException {
        message.setAttachmentIds("");
        message.setText("Mesaj sters");
        message.setStatus(REMOVED);
        try {
            messageRepository.save(message);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
        return new MessageDTO(message, principalEmail, contactEmail);
    }
}
