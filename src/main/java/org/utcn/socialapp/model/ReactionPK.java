package org.utcn.socialapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReactionPK implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "post_id", referencedColumnName = "post_id"),
            @JoinColumn(name = "author_id", referencedColumnName = "author_id")
    })
    private Post post;

    public ReactionPK() {
    }

    public ReactionPK(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReactionPK)) return false;
        ReactionPK that = (ReactionPK) o;
        return user.equals(that.user) && post.equals(that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, post);
    }
}
