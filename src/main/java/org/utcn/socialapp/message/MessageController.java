package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
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
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/message")
    @MessageExceptionHandler(BusinessException.class)
    public void sendToUser(Principal principal, SendDTO oldSendDTO) throws BusinessException {
        if (Objects.isNull(principal)) throw new BusinessException(BAD_REQUEST);
        messageService.sendToUser(principal.getName(), oldSendDTO);
    }

    @GetMapping()
    public ResponseEntity<List<MessageDTO>> getUserMessages(@RequestBody ChatDTO chatDTO) throws BusinessException {
        return ResponseEntity.ok(messageService.getConversation(chatDTO));
    }

//    @Async
//    @MessageExceptionHandler(BusinessException.class)
//    @EventListener({SessionConnectedEvent.class})
//    public void sendUserList(){
//        messageService.sendUserList();
//    }

    @Async
    @EventListener({SessionConnectedEvent.class, SessionDisconnectEvent.class})
    public void sendUserListDisconnect() throws InterruptedException {
        Thread.sleep(500);
        messageService.sendUserList();
    }

//    @Async
//    @MessageMapping("/refresh-connected")
//    public void sendUserList(){
//        messageService.sendUserList();
//    }
}
