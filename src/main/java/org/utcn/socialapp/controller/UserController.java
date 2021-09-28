package org.utcn.socialapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping
public class UserController {
    @GetMapping
    public String login() {
        return "This is login page! TBI...";
    }

    @GetMapping("profile")
    public String profile() {
        return "Profile TBI!...";
    }

    @GetMapping("admin")
    public String admin() {
        return "Admin page, hello!";
    }
}
