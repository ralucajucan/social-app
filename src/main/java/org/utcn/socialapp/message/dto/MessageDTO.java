package org.utcn.socialapp.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.utcn.socialapp.message.Message;
import org.utcn.socialapp.message.MessageStatus;

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
    private List<EmptyFileDTO> attachments;
    private MessageStatus status;
    private String createdOn;
    private String sentOn;
    private String updatedOn;

    public MessageDTO(Message message) {
        this.id = message.getMessagePK().getId();
        this.sender = message.getSender().getEmail();
        this.receiver = message.getReceiver().getEmail();
        this.text = message.getText();
        this.attachmentIds = message.getAttachmentIds();
        this.attachments = null;
        this.status = message.getStatus();
        this.createdOn = message.getAudit().getCreatedOn().toString();
        this.sentOn = message.getSentOn().toString();
        this.updatedOn = message.getAudit().getUpdatedOn().toString();
    }

    public void setAttachments(List<EmptyFileDTO> attachments) {
        this.attachments = attachments;
    }
}
