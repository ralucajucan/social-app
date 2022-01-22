package org.utcn.socialapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDTO {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
