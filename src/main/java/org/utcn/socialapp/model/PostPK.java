package org.utcn.socialapp.model;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostPK implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;

    public PostPK() {
    }

    public PostPK(Long id, Long authorId) {
        this.id = id;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostPK)) return false;
        PostPK postPK = (PostPK) o;
        return id.equals(postPK.id) && authorId.equals(postPK.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorId);
    }
}
