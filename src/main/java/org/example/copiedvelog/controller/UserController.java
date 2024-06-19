package org.example.copiedvelog.controller;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
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

    @PostMapping("/api/userreg")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("name") String name) {
        try {

            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // 실제로는 비밀번호 암호화가 필요
            user.setName(name);
            user.setEmail(email);

            userService.saveUser(user);

            return "redirect:/welcome";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
