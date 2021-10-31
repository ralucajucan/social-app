package org.utcn.socialapp.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.registration.RegisterDTO;
import org.utcn.socialapp.user.registration.token.Token;

@RestController()
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody final String username) throws UsernameNotFoundException {
        return ResponseEntity.ok(userService.loadUserByUsername(username));
    }

    @PostMapping("/register")
    public ResponseEntity<Token> register(@RequestBody final RegisterDTO registerDTO) throws BusinessException {
        return ResponseEntity.ok(userService.register(registerDTO));
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> enableUserWithToken(@RequestParam("token") String token) throws BusinessException {
        return ResponseEntity.ok(userService.enableUserWithToken(token));
    }

    @GetMapping("/profile")
    public String profile() {
        return "Profile TBI!...";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin page, hello!";
    }
}
