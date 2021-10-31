package org.utcn.socialapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.config.exception.BusinessException;
import org.utcn.socialapp.model.dto.RegisterDTO;
import org.utcn.socialapp.model.security.Token;
import org.utcn.socialapp.model.security.User;
import org.utcn.socialapp.service.security.UserService;

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
