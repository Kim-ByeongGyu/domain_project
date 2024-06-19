package org.example.copiedvelog.controller;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userreg")
public class UserRestController {
    private final UserService userService;

    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        return userService.checkUsernameExists(username);
    }

    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return userService.checkEmailExists(email);
    }
}
