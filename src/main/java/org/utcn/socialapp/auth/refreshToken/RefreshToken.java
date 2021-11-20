package org.utcn.socialapp.auth.refreshToken;

import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class RefreshToken {
    @Id
    private long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiration;

    public RefreshToken() {
    }

    public RefreshToken(User user, String token, Instant expiration) {
        this.user = user;
        this.token = token;
        this.expiration = expiration;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiration() {
        return expiration;
    }
}
