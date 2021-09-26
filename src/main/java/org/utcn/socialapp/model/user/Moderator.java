package org.utcn.socialapp.model.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "MODERATOR")
public class Moderator extends User {
    public Moderator() {
    }

    public Moderator(String email, String username, String password, String firstName, String lastName, Date birthDate, Date createdAt, Date updatedAt) {
        super(email, username, password, firstName, lastName, birthDate, createdAt, updatedAt);
    }
}
