package org.utcn.socialapp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 45)
    private String email;

    @Column(name = "username", unique = true, nullable = false, length = 45)
    private String username;

    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    private Profile profile;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "reactionPK.user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> responses = new ArrayList<>();

    @OneToMany(mappedBy = "requestPK.sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> sent = new ArrayList<>();

    @OneToMany(mappedBy = "requestPK.receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> received = new ArrayList<>();

    public User() {
    }

    public User(String email, String username, String password, Profile profile, Date createdAt, Date updatedAt, List<Reaction> responses, List<Request> sent, List<Request> received) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profile = profile;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.responses = responses;
        this.sent = sent;
        this.received = received;
    }
}
