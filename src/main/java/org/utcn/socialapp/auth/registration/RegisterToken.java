package org.utcn.socialapp.auth.registration;

import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class RegisterToken {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "token", nullable = false)
    private String token;


    @Column(nullable = false)
    private OffsetDateTime expiration;

    private OffsetDateTime confirmation;

    public RegisterToken() {
    }

    public RegisterToken(User user, String token, OffsetDateTime expiration) {
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

    public OffsetDateTime getExpiration() {
        return expiration;
    }

    public OffsetDateTime getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(OffsetDateTime confirmation) {
        this.confirmation = confirmation;
    }

    public User getUser() {
        return user;
    }
}
