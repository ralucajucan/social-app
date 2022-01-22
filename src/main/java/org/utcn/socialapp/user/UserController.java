package org.utcn.socialapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.dto.BasicDTO;
import org.utcn.socialapp.user.dto.EditDTO;
import org.utcn.socialapp.user.dto.PasswordDTO;
import org.utcn.socialapp.user.dto.UserDTO;

import java.util.List;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam int page, @RequestParam int count) throws BusinessException{
        return ResponseEntity.ok(userService.getUserPage(page,count));
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
}
