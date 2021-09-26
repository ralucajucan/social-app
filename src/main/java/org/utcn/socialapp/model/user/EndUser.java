package org.utcn.socialapp.model.user;

import org.utcn.socialapp.model.Post;
import org.utcn.socialapp.model.Request;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue(value = "END_USER")
public class EndUser extends User {

    @Column(name = "biography", nullable = false, length = 200)
    private String biography;

    @Enumerated(value = EnumType.STRING)
    private Relationship relationship;

    @Column(name = "country", nullable = false, length = 45)
    private String country;

    @Column(name = "city", nullable = false, length = 45)
    private String city;

    @Column(name = "phone_number", nullable = false, length = 45)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sender_user_id")
    private List<Request> sent = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "receiver_user_id")
    private List<Request> received = new ArrayList<>();

    @ManyToMany(mappedBy = "likes")
    private List<Post> likedPosts = new ArrayList<>();

    public EndUser() {
    }

    public EndUser(String email, String username, String password, String firstName, String lastName, Date birthDate, Date createdAt, Date updatedAt, String biography, Relationship relationship, String country, String city, String phoneNumber, Gender gender) {
        super(email, username, password, firstName, lastName, birthDate, createdAt, updatedAt);
        this.biography = biography;
        this.relationship = relationship;
        this.country = country;

        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }
}
