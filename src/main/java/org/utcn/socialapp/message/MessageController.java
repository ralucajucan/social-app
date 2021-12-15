package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.message.dto.ChatDTO;
import org.utcn.socialapp.message.dto.MessageDTO;
import org.utcn.socialapp.message.dto.SendDTO;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_REQUEST;

@Controller
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

//    @MessageMapping("/message")
//    @SendTo("/topic")
//    public ResponseEntity<MessageDTO> send(Principal principal, SendDTO sendDTO) throws BusinessException {
//        return ResponseEntity.ok(messageService.sendOne(principal.getName(), sendDTO));
//    }
    @MessageMapping("/message")
    public void sendToUser(Principal principal, SendDTO oldSendDTO) throws BusinessException {
        if(Objects.isNull(principal)) throw new BusinessException(BAD_REQUEST);
        SendDTO newSendDTO = messageService.sendToUser(principal.getName(), oldSendDTO);
        messagingTemplate.convertAndSendToUser(
                newSendDTO.getReceiverName(),
                "/queue/greetings",
                newSendDTO.getText());
        simpUserRegistry.getUsers().stream()
                .map(u -> u.getName())
                .forEach(System.out::println);
    }

    @GetMapping()
    public ResponseEntity<List<MessageDTO>> getUserMessages(@RequestBody ChatDTO chatDTO) throws BusinessException {
        return ResponseEntity.ok(messageService.getConversation(chatDTO));
    }
}
