package org.utcn.socialapp.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RequestPK implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    public RequestPK() {
    }

    public RequestPK(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestPK)) return false;
        RequestPK requestPK = (RequestPK) o;
        return sender.equals(requestPK.sender) && receiver.equals(requestPK.receiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver);
    }
}
