package org.utcn.socialapp.model;

import org.utcn.socialapp.model.security.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @EmbeddedId
    private PostPK postPK;

    @Embedded
    private Audit audit = new Audit();

    @MapsId("authorId")
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "post")
    private List<Reaction> reactions = new ArrayList<>();


    public Post() {
    }
}
