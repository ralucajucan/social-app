package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.utcn.socialapp.common.exception.BusinessException;
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
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageExceptionHandler(BusinessException.class)
    public void handleExceptions(Principal principal, Exception e) {
        messageService.sendError(principal.getName(), e);
    }

    @MessageMapping("/message")
    public void sendToUser(Principal principal, SendDTO oldSendDTO) throws BusinessException {
        if (Objects.isNull(principal)) throw new BusinessException(BAD_REQUEST);
        messageService.sendToUser(principal.getName(), oldSendDTO);
    }

    @GetMapping("/conv")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @RequestParam String sender,
            @RequestParam(required = false, defaultValue = "0") int page) throws BusinessException {
        return ResponseEntity.ok(messageService.getConversation(sender, page));
    }

//    @MessageMapping("/refresh-connected")
    @EventListener({SessionConnectedEvent.class, SessionDisconnectEvent.class})
    public void sendUserListDisconnect() throws InterruptedException {
        Thread.sleep(500);
        messageService.sendUserList();
    }

    @MessageMapping("/refresh-connected")
    public void sendUserList(){
        messageService.sendUserList();
    }
}
