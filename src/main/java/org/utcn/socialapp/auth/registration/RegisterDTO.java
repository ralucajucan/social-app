package org.utcn.socialapp.auth.registration;

import java.util.Objects;
import java.util.stream.Stream;

public class RegisterDTO {
    /**
     * Mandatory fields for creating USER and PROFILE entities.
     */
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String birthDate;

    public RegisterDTO(String email, String password, String firstName, String lastName, String birthDate) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public boolean anyMatchNull() {
        return Stream.of(email, password, firstName, lastName, birthDate)
                .anyMatch(Objects::isNull);
    }

    public String getEmail() {
        return email;
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
