package org.example.copiedvelog.controller;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/api")
    public String api(Model model) {
        return "api";
    }

    @GetMapping("/api/loginform")
    public String loginForm(Model model) {
        return "loginform";
    }

    @GetMapping("/api/userregform")
    public String userregform(Model model) {
        model.addAttribute("user", new User());
        return "userregform";
    }

    @PostMapping("/api/userreg1")
    public String registerUser(@RequestParam("userName") String userName,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("name") String name,
                               Model model) {
        User newUser = new User();
        newUser.setUsername(userName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setName(name);

        userService.saveUser(newUser);

        model.addAttribute("user", newUser);

        return "welcome";
    }
}
