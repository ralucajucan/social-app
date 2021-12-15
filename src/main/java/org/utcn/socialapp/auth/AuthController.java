package org.utcn.socialapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.auth.jwt.JWTResponse;
import org.utcn.socialapp.auth.registration.RegisterDTO;
import org.utcn.socialapp.auth.registration.RegisterToken;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.User;

@Controller()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;

    @PostMapping()
    public ResponseEntity<AuthResDTO> authenticate(@RequestBody AuthDTO authDTO) throws BusinessException {
        return ResponseEntity.ok(authService.authenticate(authDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterToken> register(@RequestBody final RegisterDTO registerDTO) throws BusinessException {
        return ResponseEntity.ok(authService.register(registerDTO));
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> enableUserWithToken(@RequestParam String token) throws BusinessException {
        return ResponseEntity.ok(authService.enableUserWithToken(token));
    }

    @GetMapping("/refresh")
    public ResponseEntity<JWTResponse> refresh(@RequestParam String token) throws BusinessException {
        return ResponseEntity.ok(authService.refreshJwtToken(token));
    }


    @DeleteMapping("/logout")
    public ResponseEntity<User> logout(@RequestParam Long userId) throws BusinessException {
        authService.logout(userId);
        return ResponseEntity.noContent()
                             .build();
    }
}
