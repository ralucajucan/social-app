package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.message.dto.ChatDTO;
import org.utcn.socialapp.message.dto.MessageDTO;
import org.utcn.socialapp.message.dto.SendDTO;

import java.util.List;

@Controller
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @MessageMapping()
    @SendTo("/topic/greetings")
    public ResponseEntity<MessageDTO> send(@RequestBody SendDTO sendDTO) throws BusinessException {
        return ResponseEntity.ok(messageService.sendOne(sendDTO));
    }

    @GetMapping()
    public ResponseEntity<List<MessageDTO>> getUserMessages(@RequestBody ChatDTO chatDTO) throws BusinessException {
        return ResponseEntity.ok(messageService.getConversation(chatDTO));
    }
}
