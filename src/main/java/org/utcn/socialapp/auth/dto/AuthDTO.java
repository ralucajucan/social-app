package org.utcn.socialapp.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.utcn.socialapp.user.User;

@Getter
@NoArgsConstructor
public class AuthDTO extends JwtDTO {
    private String refreshToken;

    public AuthDTO(
            User user,
            String jwtToken,
            String refreshToken
    ) {
        super(user, jwtToken);
        this.refreshToken = refreshToken;
    }
}
