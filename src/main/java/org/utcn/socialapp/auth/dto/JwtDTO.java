package org.utcn.socialapp.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.utcn.socialapp.user.User;

@Getter
@NoArgsConstructor
public class JwtDTO {
    private Long id;
    private String role;
    private String email;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String biography;
    private String jwtToken;

    public JwtDTO(
            User user,
            String jwtToken) {
        this.id = user.getId();
        this.role = user.getRole().toString();
        this.email = user.getEmail();
        this.firstName = user.getProfile().getFirstName();
        this.lastName = user.getProfile().getLastName();
        this.birthDate = user.getProfile().getBirthDate().toString();
        this.biography = user.getProfile().getBiography();
        this.jwtToken = jwtToken;
    }
}
