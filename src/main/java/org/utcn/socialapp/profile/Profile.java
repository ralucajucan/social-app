package org.utcn.socialapp.profile;

import org.springframework.format.annotation.DateTimeFormat;
import org.utcn.socialapp.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profile")
public class Profile {
    @Id
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(name = "biography", length = 200)
    private String biography;

    @Column(name = "country", length = 45)
    private String country;

    @Column(name = "city", length = 45)
    private String city;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Relationship relationship;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    public Profile() {
    }

    public Profile(User user, String firstName, String lastName, LocalDate birthDate) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
