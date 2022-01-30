package org.utcn.socialapp.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.utcn.socialapp.message.Message;
import org.utcn.socialapp.message.MessageStatus;

import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String sender;
    private String receiver;
    private String text;
    private String attachmentIds;
    private List<FileDTO> attachments;
    private MessageStatus status;
    private boolean edited;
    private String createdOn;
    private String updatedOn;

    public MessageDTO(Long id, String sender, String receiver, byte[] text, String attachmentIds,
                      MessageStatus status, boolean edited, Instant createdOn, Instant updatedOn) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = new String(text);
        this.attachmentIds = attachmentIds;
        this.status = status;
        this.edited = edited;
        this.createdOn = createdOn.toString();
        this.updatedOn = updatedOn.toString();
    }

    public MessageDTO(Message message, String senderEmail, String receiverEmail) {
        this.id = message.getMessagePK().getId();
        this.sender = senderEmail;
        this.receiver = receiverEmail;
        this.text = message.getText();
        this.attachmentIds = message.getAttachmentIds();
        this.attachments = null;
        this.status = message.getStatus();
        this.edited = message.isEdited();
        this.createdOn = message.getAudit().getCreatedOn().toString();
        this.updatedOn = message.getAudit().getUpdatedOn().toString();
    }

    public void setAttachments(List<FileDTO> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", attachmentIds='" + attachmentIds + '\'' +
                ", attachments=" + attachments +
                ", status=" + status +
                ", edited=" + edited +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}
