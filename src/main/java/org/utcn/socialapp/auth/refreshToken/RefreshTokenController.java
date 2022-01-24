package org.utcn.socialapp.auth.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.utcn.socialapp.auth.dto.JwtDTO;
import org.utcn.socialapp.common.exception.BusinessException;

@Controller
@RequestMapping("/api/auth/refresh")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @GetMapping()
    public ResponseEntity<JwtDTO> refresh(@RequestParam String token) throws BusinessException {
        return ResponseEntity.ok(refreshTokenService.refreshJwtToken(token));
    }
}
