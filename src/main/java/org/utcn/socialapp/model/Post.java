package org.utcn.socialapp.model;

import org.utcn.socialapp.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @EmbeddedId
    private PostPK postPK;

    @ManyToMany
    @JoinTable(name = "liked_post",
            joinColumns = {@JoinColumn(name = "post_id"),
                    @JoinColumn(name = "author_user_id")},
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> likes = new ArrayList<>();
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
}
