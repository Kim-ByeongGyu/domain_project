package org.example.copiedvelog.controller;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.Post;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.example.copiedvelog.service.PostService;
import org.example.copiedvelog.service.UserService;
import org.example.copiedvelog.service.VelogService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final VelogService velogService;
    private final UserService userService;

    @GetMapping("/posts/{postId}/like")
    public String toggleLike(@PathVariable Long postId, RedirectAttributes redirectAttributes) {
        // 현재 로그인한 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        postService.toggleLike(postId, username);
        postService.decrementViews(postId);

        // Velog 엔티티 가져오기
        Velog velog = postService.getVelogByPostId(postId);
        String velogName = velog.getName();

        redirectAttributes.addAttribute("username", username);
        redirectAttributes.addAttribute("velogName", velogName);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/{username}/velog/{velogName}/postView/{postId}";
    }
}