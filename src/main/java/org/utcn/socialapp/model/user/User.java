package org.utcn.socialapp.model.user;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email", nullable = false, length = 45)
    private String email;
    @Column(name = "username", nullable = false, length = 45)
    private String username;
    @Column(name = "password", nullable = false, length = 45)
    private String password;
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Column(name = "birth_date", nullable = false)
    private Date birthDate;
    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();

    public User() {
    }

    public User(String email, String username, String password, String firstName, String lastName, Date birthDate, Date createdAt, Date updatedAt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
