package org.example.copiedvelog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.copiedvelog.entity.Post;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.example.copiedvelog.repository.UserRepository;
import org.example.copiedvelog.security.CustomUserDetails;
import org.example.copiedvelog.service.PostService;
import org.example.copiedvelog.service.UserService;
import org.example.copiedvelog.service.VelogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VelogController {
    private final VelogService velogService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/{username}/userinfo")
    @Transactional(readOnly = true)
    public String userInfo(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails != null) {
            User user = userService.findByUsername(userDetails.getUsername());
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

    @GetMapping("/{username}/createvelog")
    public String createVelog(Model model) {
        model.addAttribute("velog", new Velog());
        return "createvelogform";
    }
    @PostMapping("/{username}/velogreg")
    public String registerVelog(@ModelAttribute Velog velog, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "loginform"; // 로그인 폼 페이지로 리다이렉트
        }

        User user = userService.findByUsername(userDetails.getUsername());
        Long userId = user.getId();
        user.setId(userId);

        velog.setOwner(user);
        velogService.saveVelog(velog);

        return "redirect:/{username}/userinfo";
    }

    @GetMapping("/{username}/velog/{velogName}")
    @Transactional(readOnly = true)
    public String velogDetail(@PathVariable String username, @PathVariable String velogName, Model model, HttpServletRequest request) {
        Velog velog = velogService.findByName(velogName);
        if (velog == null || !velog.getOwner().getUsername().equals(username)) {
            model.addAttribute("error", "블로그를 찾을 수 없습니다.");
            return "error";
        }

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (userDetails != null) {
            user = userService.findByUsername(userDetails.getUsername());
        }

        List<Post> posts = postService.findPostsByUserAndVelog(user, velog);

        model.addAttribute("velog", velog);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "velog";
    }

    @GetMapping("/{username}/velog/{velogName}/posts/new")
    public String showCreatePostForm(@PathVariable String username,
                                     @PathVariable String velogName,
                                     Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("username", username);
        model.addAttribute("velogName", velogName);
        return "createPost";
    }

    @PostMapping("/{username}/velog/{velogName}/createposts")
    public String createPost(@PathVariable String username, @PathVariable String velogName, @ModelAttribute Post post) {
        post.setUser(userService.findByUsername(username));
        post.setVelog(velogService.findByName(velogName));
        postService.createPost(post);
        return "redirect:/{username}/velog/{velogName}";
    }

    @GetMapping("/{username}/velog/{velogName}/postView/{postId}")
    public String getPost(@PathVariable String username,
                          @PathVariable String velogName,
                          @PathVariable Long postId,
                          Principal principal,
                          Model model) {
        User user = userService.findByUsername(username);
        Velog velog = velogService.findByName(velogName);

        Optional<Post> post = postService.findByIdAndUserAndVelog(postId, user, velog);

        if (post.isPresent()) {
            model.addAttribute("post", post.get());

            return "postcontent"; // 실제 포스트를 보여줄 HTML 파일 이름
        } else {
            return "error"; // 에러 페이지 이름
        }
    }
}
