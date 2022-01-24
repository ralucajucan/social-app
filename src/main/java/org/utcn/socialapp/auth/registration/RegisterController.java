package org.utcn.socialapp.auth.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.common.exception.BusinessException;

@Controller()
@RequiredArgsConstructor
@RequestMapping("/api/auth/register")
public class RegisterController {
    public final RegisterService registerService;

    @PostMapping()
    public ResponseEntity<Register> register(@RequestBody final RegisterDTO registerDTO) throws BusinessException {
        return ResponseEntity.ok(registerService.register(registerDTO));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> enableUserWithToken(@RequestParam String token) throws BusinessException {
        return ResponseEntity.ok(registerService.enableUserWithToken(token));
    }

    @GetMapping("/reset")
    public ResponseEntity<?> resetRegisterToken(@RequestParam String email) throws BusinessException{
        return ResponseEntity.ok(registerService.resetRegisterToken(email.trim()));
    }
}
