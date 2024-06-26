package org.example.copiedvelog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.config.UserContext;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.example.copiedvelog.repository.UserRepository;
import org.example.copiedvelog.service.UserService;
import org.example.copiedvelog.service.VelogService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VelogController {
    private final VelogService velogService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/api/{username}/userinfo")
    @Transactional(readOnly = true)
    public String userInfo(Model model) {
        User user = UserContext.getUser();
;
        if (user != null) {
            user = userService.findByUsername(user.getUsername());
            if (user == null) {
                return "redirect:/loginform";
            }

            List<Velog> velogs = velogService.findByOwner(user);
            model.addAttribute("velogs", velogs);

            model.addAttribute("username", user.getUsername());

            return "userinfo";
        } else {
            return "redirect:/loginform";
        }
    }

    @GetMapping("/api/{username}/createvelog")
    public String createVelog(Model model) {
        model.addAttribute("velog", new Velog());
        return "createvelogform";
    }
    @PostMapping("/api/{username}/velogreg")
    public String registerVelog(@ModelAttribute Velog velog, Model model) {
        User user = UserContext.getUser();
//        User user_owner = userService.findByUsername(user.getUsername());
        Long userId = userService.findByUsername(user.getUsername()).getId();
        user.setId(userId);
        if (user == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "loginform"; // 로그인 폼 페이지로 리다이렉트
        }

        velog.setOwner(user);
        velogService.saveVelog(velog);

        return "redirect:/api/{username}/userinfo";
    }

    @GetMapping("/api/{username}/velog/{velogName}")
    @Transactional(readOnly = true)
    public String velogDetail(@PathVariable String username, @PathVariable String velogName, Model model, HttpServletRequest request) {
        Velog velog = velogService.findByName(velogName);
        if (velog == null || !velog.getOwner().getUsername().equals(username)) {
            model.addAttribute("error", "블로그를 찾을 수 없습니다.");
            return "error";
        }

        User user = UserContext.getUser();

        model.addAttribute("velog", velog);
        model.addAttribute("user", user);
        return "velog";
    }
}
