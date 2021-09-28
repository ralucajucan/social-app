package org.utcn.socialapp.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "profile")
public class Profile {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "subdirectory", nullable = false)
    private String subdirectory;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

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

}
