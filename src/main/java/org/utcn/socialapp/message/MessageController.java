package org.utcn.socialapp.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.message.attachment.AttachmentService;
import org.utcn.socialapp.message.dto.FileDTO;
import org.utcn.socialapp.message.dto.MessageDTO;
import org.utcn.socialapp.message.dto.SendDTO;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.UNAUTHORIZED;

@Controller
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final AttachmentService attachmentService;

    @MessageExceptionHandler(BusinessException.class)
    public void handleExceptions(Principal principal, Exception e) {
        if (principal == null) {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            messageService.sendError(sessionId, e);
        } else {
            messageService.sendError(principal.getName(), e);
        }
    }

    @MessageMapping("/message")
    public void sendToUser(Principal principal, SendDTO sendDTO) throws BusinessException {
        if (Objects.isNull(principal)) throw new BusinessException(UNAUTHORIZED);
        messageService.sendToUser(principal.getName(), sendDTO);
    }

    @GetMapping("/conv")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @RequestParam String user,
            @RequestParam(required = false, defaultValue = "0") int page) throws BusinessException {
        List<MessageDTO> messageDTOS = messageService.getConversation(user, page);
        messageDTOS.stream()
                .forEach(message -> message.setAttachments(
                        attachmentService.getFilesWithoutContent(message.getAttachmentIds())));
        return ResponseEntity.ok(messageDTOS);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(attachmentService.addFile(file));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws BusinessException {
        FileDTO fileDTO = attachmentService.getFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDTO.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getName() + "\"")
                .body(new ByteArrayResource(fileDTO.getFile()));
    }

    @EventListener()
    public void onConnect(SessionConnectedEvent event) {
        Principal principal = event.getUser();
        if (Objects.isNull(principal)) {
            String sessionId = "" + event.getMessage().getHeaders().get("simpSessionId");
            messageService.sendError(sessionId, new BusinessException(UNAUTHORIZED));
        } else {
            messageService.updateSentAsReceived(principal.getName());
            messageService.sendUserList();
        }
    }

    @EventListener({SessionDisconnectEvent.class})
    public void onDisconnect() {
        messageService.sendUserList();
    }

    @MessageMapping("/refresh-connected")
    public void refreshUserList() {
        messageService.sendUserList();
    }
}
