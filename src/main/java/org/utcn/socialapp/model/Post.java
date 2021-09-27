package org.utcn.socialapp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @EmbeddedId
    private PostPK postPK;

    @OneToMany(mappedBy = "reactionPK.post")
    private List<Reaction> reactions = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();
}
