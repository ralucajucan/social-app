package org.utcn.socialapp.model;

import org.utcn.socialapp.model.user.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RequestPK implements Serializable {
    @ManyToOne
    private User sender;
    @ManyToOne
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
