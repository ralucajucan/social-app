package org.utcn.socialapp.user.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RequestPK implements Serializable {
    private Long senderId;
    private Long receiverId;

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestPK)) return false;
        RequestPK requestPK = (RequestPK) o;
        return senderId.equals(requestPK.senderId) && receiverId.equals(requestPK.receiverId);
    }
}
