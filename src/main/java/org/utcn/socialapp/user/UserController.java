package org.utcn.socialapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.dto.BasicDTO;
import org.utcn.socialapp.user.dto.PasswordDTO;

import java.util.List;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;
    @GetMapping()
    public ResponseEntity<List<BasicDTO>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody final PasswordDTO passwordDTO) throws BusinessException {
        userService.changePassword(passwordDTO);
        return ResponseEntity.ok().body(null);
    }
}
