package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.message.dto.ChatDTO;
import org.utcn.socialapp.message.dto.MessageDTO;
import org.utcn.socialapp.message.dto.SendDTO;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_REQUEST;
import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final static int MESSAGE_COUNT = 15;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private MessageDTO newMessageDTO(Message message){
        return new MessageDTO(
                message.getMessagePK().getId(),
                message.getMessagePK().getSenderId(),
                message.getMessagePK().getReceiverId(),
                message.getText(),
                message.getAudit().getCreatedOn(),
                message.getAudit().getUpdatedOn()
        );
    }

    public MessageDTO sendOne(SendDTO sendDTO) throws BusinessException {
        User sender = userRepository
                .findById(sendDTO.getSender())
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        String senderName = userRepository
                .findProfileByUserId(sender.getId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .getName();
        User receiver = userRepository
                .findById(sendDTO.getReceiver())
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        String receiverName = userRepository
                .findProfileByUserId(receiver.getId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .getName();
        Long messageId = messageRepository.countAllBySenderAndReceiver(sender, receiver);
        Message message = new Message(messageId, sender, receiver, sendDTO.getText());
        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(senderName, receiverName, message.getText());
        return newMessageDTO(message);
    }

    public List<MessageDTO>  getConversation(ChatDTO chatDTO) throws BusinessException {
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
