package org.utcn.socialapp.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.auth.registration.RegisterDTO;
import org.utcn.socialapp.auth.registration.RegisterToken;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {
    public final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping()
    public ResponseEntity<String> authenticate(@RequestBody AuthDTO authDTO) throws BusinessException {
        return ResponseEntity.ok(authService.authenticate(authDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterToken> register(@RequestBody final RegisterDTO registerDTO) throws BusinessException {
        return ResponseEntity.ok(authService.register(registerDTO));
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> enableUserWithToken(@RequestParam("token") String token) throws BusinessException {
        return ResponseEntity.ok(authService.enableUserWithToken(token));
    }
}
