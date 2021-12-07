package org.utcn.socialapp.auth.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    /**
     * Mandatory fields for creating USER and PROFILE entities.
     */
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String birthDate;

    public boolean anyMatchNull() {
        return Stream.of(email, password, firstName, lastName, birthDate).anyMatch(Objects::isNull);
    }
}
