package org.utcn.socialapp.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.utcn.socialapp.common.utils.Audit;
import org.utcn.socialapp.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Message {
    @EmbeddedId
    private MessagePK messagePK;

    @Embedded
    private final Audit audit = new Audit();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("senderId")
    @JoinColumn(name="sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiverId")
    @JoinColumn(name="receiver_id", referencedColumnName = "id")
    private User receiver;

    private String text;

    public Message(Long id, User sender, User receiver, String text) {
        this.messagePK = new MessagePK(id,sender.getId(), receiver.getId());
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }
}
