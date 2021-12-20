package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.message.dto.ChatDTO;
import org.utcn.socialapp.message.dto.MessageDTO;
import org.utcn.socialapp.message.dto.SendDTO;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_REQUEST;
import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final static int MESSAGE_COUNT = 15;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private MessageDTO newMessageDTO(Message message) {
        return new MessageDTO(
                message.getMessagePK().getId(),
                message.getSender().getEmail(),
                message.getReceiver().getEmail(),
                message.getText(),
                message.getAudit().getCreatedOn(),
                message.getAudit().getUpdatedOn()
        );
    }

    public List<String> getUserList(){
        return simpUserRegistry.getUsers().stream().map(
                user -> user.getName()).collect(Collectors.toList());
    }

    public void sendUserList(){
        List<String> usersList = getUserList();
        usersList.stream().forEach(
                user -> simpMessagingTemplate.convertAndSendToUser(
                        user,
                        "/queue/list",
                        usersList)
        );
    }

    @Transactional
    public void sendToUser(String principalEmail, SendDTO sendDTO) throws BusinessException {
        if (!StringUtils.hasLength(principalEmail) || sendDTO.requiredMatchNull()){
            throw new BusinessException(BAD_REQUEST);
        }
        User sender = userRepository.findByEmail(principalEmail);

        User receiver = userRepository.findByEmail(sendDTO.getReceiver());
        Long messageId = messageRepository.countAllBySenderAndReceiver(sender, receiver);
        Message message = new Message(messageId, sender, receiver, sendDTO.getText());
        messageRepository.save(message);
        MessageDTO messageDTO= newMessageDTO(messageRepository.findById(message.getMessagePK())
                .orElseThrow(() -> new BusinessException(BAD_REQUEST)));
        simpMessagingTemplate.convertAndSendToUser(
                messageDTO.getReceiver(),
                "/queue/greetings",
                messageDTO);
    }

    public List<MessageDTO> getConversation(ChatDTO chatDTO) throws BusinessException {
        if (chatDTO.requiredMatchNull() || chatDTO.pageIsNegative()) {
            throw new BusinessException(BAD_REQUEST);
        }
        User sender = userRepository
                .findById(chatDTO.getSender())
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        User receiver = userRepository
                .findById(chatDTO.getReceiver())
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        Pageable pageable = PageRequest.of(chatDTO.getPage(), MESSAGE_COUNT, Sort.by("sender").descending());

        return messageRepository
                .findConversation(sender, receiver, pageable)
                .stream()
                .map(message -> newMessageDTO(message))
                .collect(Collectors.toList());
    }
}
