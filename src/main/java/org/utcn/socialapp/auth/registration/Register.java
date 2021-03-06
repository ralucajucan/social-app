package org.utcn.socialapp.auth.registration;

import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Register {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;


    @Column(nullable = false)
    private Instant expiration;

    private Instant confirmation;

    public Register() {
    }

    public Register(User user, String token, Instant expiration) {
        this.user = user;
        this.token = token;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public Instant getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Instant confirmation) {
        this.confirmation = confirmation;
    }

    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }
}
