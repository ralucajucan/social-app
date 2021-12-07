package org.utcn.socialapp.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResDTO {
    private final String type = "Bearer";
    private String jwtToken;
    private String refreshToken;
    private Long userId;
    private String email;
    private String role;
}
