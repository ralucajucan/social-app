package org.utcn.socialapp.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reaction")
public class Reaction {
    @EmbeddedId
    private ReactionPK reactionPK;

    @Enumerated(value = EnumType.STRING)
    private ReactionStatus reactionStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();
}
