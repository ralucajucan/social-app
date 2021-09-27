package org.utcn.socialapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostPK implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private Long postId;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private User author;

    public PostPK() {
    }

    public PostPK(User author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostPK)) return false;
        PostPK postPK = (PostPK) o;
        return postId.equals(postPK.postId) && author.equals(postPK.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, author);
    }
}
