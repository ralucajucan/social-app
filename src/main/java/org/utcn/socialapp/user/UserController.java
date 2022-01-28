package org.utcn.socialapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.dto.EditDTO;
import org.utcn.socialapp.user.dto.PasswordDTO;
import org.utcn.socialapp.user.dto.UserDTO;
import org.utcn.socialapp.user.dto.UserPageDTO;

import java.util.List;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserPageDTO> getUsers(@RequestParam int page, @RequestParam int count) throws BusinessException {
        return ResponseEntity.ok(userService.getUserPage(page, count));
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody final PasswordDTO passwordDTO) throws BusinessException {
        userService.changePassword(passwordDTO);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> changeSelected(@RequestBody final EditDTO editDTO) throws BusinessException {
        userService.changeSelected(editDTO);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteById(@RequestParam final Long id) throws BusinessException {
        userService.deleteUser(id);
        return ResponseEntity.ok().body(null);
    }
}
