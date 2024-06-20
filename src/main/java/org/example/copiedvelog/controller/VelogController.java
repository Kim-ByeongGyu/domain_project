package org.example.copiedvelog.controller;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.service.VelogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class VelogController {
    private final VelogService velogService;

    @GetMapping("/api/user/userinfo")
    public String userInfo() {
        return "userinfo";
    }

    @GetMapping("/api/user/createvelog")
    public String createVelog() {
        return "createvelogform";
    }
}
