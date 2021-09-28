package org.utcn.socialapp.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RequestPK implements Serializable {
    private Long senderId;
    private Long receiverId;

    public RequestPK() {
    }

    public RequestPK(Long senderId, Long receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestPK)) return false;
        RequestPK requestPK = (RequestPK) o;
        return senderId.equals(requestPK.senderId) && receiverId.equals(requestPK.receiverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId);
    }
}
