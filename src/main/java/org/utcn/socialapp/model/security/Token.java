package org.utcn.socialapp.model.security;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token")
public class Token {
    @Id
    @SequenceGenerator(
            name = "token_sequence",
            sequenceName = "token_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    private Long id;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime creation;

    @Column(nullable = false)
    private LocalDateTime expiration;

    private LocalDateTime confirmation;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Token() {
    }

    public Token(String uuid, LocalDateTime creation, LocalDateTime expiration, User user) {
        this.uuid = uuid;
        this.creation = creation;
        this.expiration = expiration;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public LocalDateTime getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(LocalDateTime confirmation) {
        this.confirmation = confirmation;
    }

    public User getUser() {
        return user;
    }
}
