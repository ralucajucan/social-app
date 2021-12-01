package org.utcn.socialapp.auth.refreshToken;

import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class RefreshToken {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private OffsetDateTime expiration;

    public RefreshToken() {
    }

    public RefreshToken(User user, String token, OffsetDateTime expiration) {
        this.user = user;
        this.token = token;
        this.expiration = expiration;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiration(OffsetDateTime expiration) {
        this.expiration = expiration;
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public OffsetDateTime getExpiration() {
        return expiration;
    }
}
