package org.example.copiedvelog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.config.UserContext;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.example.copiedvelog.service.VelogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VelogController {
    private final VelogService velogService;

    @GetMapping("/api/{username}/userinfo")
    public String userInfo(HttpServletRequest request, Model model) {
        User user = UserContext.getUser();
        if (user != null) {
//            List<Velog> velogs = velogService.findByOwner(user);
//            model.addAttribute("velogs", velogs);
            return "userinfo";
        }
        else {
            return "redirect:/loginform";
        }
    }

    @GetMapping("/api/{username}/createvelog")
    public String createVelog(Model model) {
        model.addAttribute("velog", new Velog());
        return "createvelogform";
    }
    @PostMapping("/api/{username}/velogreg")
    public String registerVelog(@ModelAttribute Velog velog, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "loginform"; // 로그인 폼 페이지로 리다이렉트
        }

        velog.setOwner(user);
        velogService.saveVelog(velog);

        return "redirect:/api/{username}/userinfo";
    }

    @GetMapping("/api/{username}/velog/{velogName}")
    public String velogDetail(@PathVariable String username, @PathVariable String velogName, Model model, HttpServletRequest request) {
        Velog velog = velogService.findByName(velogName);
        if (velog == null || !velog.getOwner().getUsername().equals(username)) {
            model.addAttribute("error", "블로그를 찾을 수 없습니다.");
            return "error";
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        model.addAttribute("velog", velog);
        model.addAttribute("user", user);
        return "velog";
    }
}
