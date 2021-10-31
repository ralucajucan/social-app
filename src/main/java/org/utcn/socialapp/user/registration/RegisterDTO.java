package org.utcn.socialapp.user.registration;

import java.util.Objects;
import java.util.stream.Stream;

public class RegisterDTO {
    /**
     * Mandatory fields for creating USER and PROFILE entities.
     */
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String birthDate;

    public RegisterDTO() {
    }

    public boolean anyMatchNull() {
        return Stream.of(email, username, password, firstName, lastName, birthDate)
                .anyMatch(Objects::isNull);
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
