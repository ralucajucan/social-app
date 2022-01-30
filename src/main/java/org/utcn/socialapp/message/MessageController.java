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
import org.springframework.util.StringUtils;
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
        MessageDTO messageDTO = messageService.newSendMessage(principal.getName(), sendDTO);
        if (StringUtils.hasLength(messageDTO.getAttachmentIds())) {
            List<FileDTO> fileDTOS = attachmentService.getFiles(messageDTO.getAttachmentIds(), false);
            fileDTOS.forEach(file -> file.setFile(null));
            messageDTO.setAttachments(fileDTOS);
        }
        messageService.sendToUser(messageDTO.getReceiver(), messageDTO);
        if (!messageDTO.getReceiver().equals(messageDTO.getSender())) {
            messageService.sendToUser(messageDTO.getSender(), messageDTO);
        }
    }

    @MessageMapping("/draft")
    public void saveDraft(Principal principal, SendDTO sendDTO) throws BusinessException {
        messageService.saveDraft(principal.getName(), sendDTO.getUser(), sendDTO.getText(), "");
    }

    @PostMapping("/edit")
    public ResponseEntity<MessageDTO> editMessage(Principal principal, @RequestBody SendDTO sendDTO,
                                                  @RequestParam Long id) throws BusinessException {
        MessageDTO messageDTO = messageService.saveEdit(id, principal.getName(), sendDTO);
        messageDTO.setAttachments(attachmentService.getFiles(messageDTO.getAttachmentIds(), false));
        return ResponseEntity.ok(messageDTO);
    }

    @GetMapping("/conv")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @RequestParam String user,
            @RequestParam(required = false, defaultValue = "0") int page) throws BusinessException {
        List<MessageDTO> messageDTOS = messageService.getConversation(user, page);
        messageDTOS
                .forEach(message -> message.setAttachments(
                        attachmentService.getFiles(message.getAttachmentIds(), false)));
        return ResponseEntity.ok(messageDTOS);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(Principal principal, @RequestParam String toEmail,
                                    @RequestParam("file") MultipartFile file) throws IOException, BusinessException {
        String attachmentId = attachmentService.addFile(file);
        messageService.saveDraft(principal.getName(), toEmail, "", attachmentId);
        return ResponseEntity.ok(attachmentId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws BusinessException {
        FileDTO fileDTO = attachmentService.getFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDTO.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getName() + "\"")
                .body(new ByteArrayResource(fileDTO.getFile()));
    }

    @DeleteMapping("/attachment")
    public ResponseEntity<?> deleteAttachment(@RequestParam String id) throws BusinessException {
        this.attachmentService.delete(id);
        this.messageService.removeAttachmentId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteMessage(Principal principal, @RequestParam Long id, @RequestParam String contactEmail) throws BusinessException {
        Message message = messageService.getMessage(id, principal.getName(), contactEmail);
        String attachmentIds = message.getAttachmentIds();
        if (StringUtils.hasLength(attachmentIds)) {
            attachmentService.deleteMultiple(attachmentIds);
        }
        return ResponseEntity.ok(messageService.softDelete(message, principal.getName(), contactEmail));
    }

    @EventListener()
    public void onConnect(SessionConnectedEvent event) {
        Principal principal = event.getUser();
        if (Objects.isNull(principal)) {
            String sessionId = "" + event.getMessage().getHeaders().get("simpSessionId");
            messageService.sendError(sessionId, new BusinessException(UNAUTHORIZED));
        } else {
            messageService.updateSentAsReceived(principal.getName());
        }
    }

    @EventListener({SessionDisconnectEvent.class})
    public void onDisconnect() {
        messageService.sendUserList(false);
    }

    @MessageMapping("/refresh-connected")
    public void refreshUserList() {
        messageService.sendUserList(true);
    }
}
