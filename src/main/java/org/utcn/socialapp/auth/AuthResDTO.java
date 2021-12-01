package org.utcn.socialapp.auth;

public class AuthResDTO {
    private final String jwtToken;
    private final String type = "Bearer";
    private final String refreshToken;
    private final Long userId;
    private final String email;
    private final String role;

    public AuthResDTO(String jwtToken, String refreshToken, Long userId, String email, String role) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getType() {
        return type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
