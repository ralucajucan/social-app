package org.utcn.socialapp.post;

import org.utcn.socialapp.common.utils.Audit;
import org.utcn.socialapp.post.reaction.Reaction;
import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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
