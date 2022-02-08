package org.utcn.socialapp.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessagePK implements Serializable {
    private Long id;
    private Long senderId;
    private Long receiverId;

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessagePK)) return false;
        MessagePK messagePK = (MessagePK) o;
        return id.equals(messagePK.id)
                && senderId.equals(messagePK.senderId)
                && receiverId.equals(messagePK.receiverId);
    }
}
