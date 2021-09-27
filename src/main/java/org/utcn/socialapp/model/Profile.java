package org.utcn.socialapp.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id")
    private Long id;

    @OneToOne(mappedBy = "profile")
    private User user;

    @Column(name = "subdirectory", nullable = false)
    private String subdirectory;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

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

    public Profile() {
    }

    public Profile(String subdirectory, String firstName, String lastName, Date birthDate, String biography, Relationship relationship, String country, String city, String phoneNumber, Gender gender) {
        this.subdirectory = subdirectory;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.biography = biography;
        this.relationship = relationship;
        this.country = country;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }
}
