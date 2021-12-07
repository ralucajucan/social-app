package org.utcn.socialapp.profile;

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
public class Profile {
    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "biography", length = 200)
    private String biography;

    @Column(name = "country", length = 45)
    private String country;

    @Column(name = "city", length = 45)
    private String city;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

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
