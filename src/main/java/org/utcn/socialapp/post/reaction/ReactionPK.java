package org.utcn.socialapp.post.reaction;

import org.utcn.socialapp.post.PostPK;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReactionPK implements Serializable {
    private Long userId;
    @Embedded
    private PostPK postId;

    public ReactionPK() {
    }

    public ReactionPK(Long userId, PostPK postId) {
        this.userId = userId;
        this.postId = postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReactionPK)) return false;
        ReactionPK that = (ReactionPK) o;
        return userId.equals(that.userId) && postId.equals(that.postId);
    }
}
