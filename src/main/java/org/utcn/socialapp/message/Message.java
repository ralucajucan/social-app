package org.utcn.socialapp.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.utcn.socialapp.common.utils.Audit;
import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class Message {
    @EmbeddedId
    private MessagePK messagePK;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("senderId")
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiverId")
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @Getter(AccessLevel.NONE)
    @Column(columnDefinition = "MEDIUMBLOB")
    @ColumnTransformer(
            read = "AES_DECRYPT(text, UNHEX('A86543E46CE18A740046B0E3A8E520CA'))",
            write = "AES_ENCRYPT(?, UNHEX('A86543E46CE18A740046B0E3A8E520CA'))"
    )
    private byte[] text;

    @Embedded
    private final Audit audit = new Audit();

    private boolean edited = false;

    /**
     * Stored attachments MongoDB ObjectIds
     * separator is ",", if file > 15 MB an "!" will be present before id
     * maximum 9 ids -> 1 id = 24 char
     */
    private String attachmentIds;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private MessageStatus status;

    public Message(Long id, User sender, User receiver, String text, String attachmentIds,
                   MessageStatus status) {
        this.messagePK = new MessagePK(id, sender.getId(), receiver.getId());
        this.sender = sender;
        this.receiver = receiver;
        this.text = text.getBytes();
        this.attachmentIds = attachmentIds;
        this.status = status;
    }

    public String getText() {
        return new String(text);
    }

    public void setText(String text) {
        this.text = text.getBytes();
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public void setEdited(boolean editedOn) {
        this.edited = editedOn;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
