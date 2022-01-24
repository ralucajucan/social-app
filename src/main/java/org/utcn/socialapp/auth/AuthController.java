package org.utcn.socialapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.auth.dto.AuthDTO;
import org.utcn.socialapp.auth.dto.CredentialsDTO;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.User;

@Controller()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;

    @PostMapping()
    public ResponseEntity<AuthDTO> authenticate(@RequestBody CredentialsDTO credentialsDTO) throws BusinessException {
        return ResponseEntity.ok(authService.authenticate(credentialsDTO));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<User> logout(@RequestParam Long userId) throws BusinessException {
        authService.logout(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) throws BusinessException{
        return ResponseEntity.ok(authService.resetPassword(email.trim()));
    }
}
