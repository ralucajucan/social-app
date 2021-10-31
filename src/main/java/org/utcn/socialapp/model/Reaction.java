package org.utcn.socialapp.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.utcn.socialapp.model.security.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reaction")
public class Reaction {
    @EmbeddedId
    private ReactionPK reactionPK;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "post_id", referencedColumnName = "id"),
            @JoinColumn(name = "author_id", referencedColumnName = "author_id")
    })
    private Post post;

    @Enumerated(value = EnumType.STRING)
    private ReactionStatus reactionStatus;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Reaction() {
    }
}
