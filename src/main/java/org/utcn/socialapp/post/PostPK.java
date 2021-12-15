package org.utcn.socialapp.post;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PostPK implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;

    @Override
    public int hashCode() {
        return Objects.hash(id, authorId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof PostPK)) return false;
        PostPK postPK = (PostPK) o;
        return id.equals(postPK.id) && authorId.equals(postPK.authorId);
    }
}
