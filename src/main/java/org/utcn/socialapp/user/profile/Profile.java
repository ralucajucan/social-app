package org.utcn.socialapp.user.profile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class
Profile {
    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false, length = 45)
    private String firstName;

    @Column(nullable = false, length = 45)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    private String biography;

    public Profile(User user, String firstName, String lastName, LocalDate birthDate) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}
