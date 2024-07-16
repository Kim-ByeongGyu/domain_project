package org.example.copiedvelog.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.copiedvelog.dto.UserLoginResponseDto;
import org.example.copiedvelog.entity.RefreshToken;
import org.example.copiedvelog.entity.Role;
import org.example.copiedvelog.entity.SocialLoginInfo;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.security.CustomUserDetails;
import org.example.copiedvelog.security.jwt.util.JwtTokenizer;
import org.example.copiedvelog.service.RefreshTokenService;
import org.example.copiedvelog.service.SocialLoginInfoService;
import org.example.copiedvelog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final SocialLoginInfoService socialLoginInfoService;

    @GetMapping("/api")
    public String api(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            Optional<Cookie> accessTokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .findFirst();

            if (accessTokenCookie.isPresent()) {
                String token = accessTokenCookie.get().getValue();
                Claims claims = jwtTokenizer.parseAccessToken(token);

                if (claims != null) {
                    // 유효한 토큰
                    String username = claims.getSubject();
                    UserLoginResponseDto userInfo = UserLoginResponseDto.builder()
                            .name(username)
                            .build(); // 최소한의 정보만 담음
                    model.addAttribute("authUser", userInfo); // 최소한의 사용자 정보를 모델에 추가
                }
            }
        }
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
    public String userreg(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/api/userregform";
        }
        User byUsername = userService.findByUsername(user.getUsername());
        if (byUsername != null) {
            result.rejectValue("username", "username.exists", "Username already exists");
            return "/error";
        }
        userService.registUser(user);

        return "redirect:/api";
    }


    @GetMapping("/welcome")
    public String welcome() {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
        if (userDetails != null)
            return "welcome";
        else
            return "redirect:/loginform";
    }
    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/api/logout")
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 삭제

        response.addCookie(cookie);
        return "redirect:/api"; // 로그아웃 후 API 페이지로 리다이렉트
    }
    @PostMapping("/api/user/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpServletResponse response,
                            Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "없는 아이디입니다.");
            return "loginform";
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "잘못된 비밀번호입니다.");
            return "loginform";
        } else {
            // 롤 객체를 꺼내서 롤의 이름만 리스트로 얻어준다.
            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            // 토큰 발급
            String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
            String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);

            // refreshToken을 DB에 저장
            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setValue(refreshToken);
            refreshTokenEntity.setUserId(user.getId());
            refreshTokenService.addRefreshToken(refreshTokenEntity);

            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000));

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
    //        refreshTokenCookie.setSecure(true); // Use true if using HTTPS
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT / 1000)); // 7 days

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            return "redirect:/api";
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키로부터 리프레쉬 토큰을 얻어온다
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // 2-1. 없다
        if (refreshToken == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // 2-2. 있다
        // 3. 토큰으로부터 정보를 얻어온다,
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        Long userId = Long.valueOf((Integer) claims.get("userId"));

        User user = userService.getUser(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못했습니다."));

        // 4. accessToken 생성
        List roles = (List) claims.get("roles");

        String accessToken = jwtTokenizer.createAccessToken(userId, user.getEmail(), user.getName(), user.getUsername(), roles);

        // 5. 쿠키 생성 response로 보내고
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT) / 1000);

        response.addCookie(accessTokenCookie);

        //6. 적절한 응답결과(ResponseEntity)를 생성해서 응답
        UserLoginResponseDto responseDto = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .name(user.getName())
                .build();

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @GetMapping("/registerSocialUser")
    public String registerSocialUser(@RequestParam("provider")String provider,
                                     @RequestParam("socialId")String socialId,
                                     @RequestParam("uuid")String uuid,
                                     @RequestParam("name")String name,
                                     Model model) {
        model.addAttribute("provider", provider);
        model.addAttribute("socialId", socialId);
        model.addAttribute("uuid", uuid);
        model.addAttribute("name", name);

        return "users/registerSocialUser";
    }

    @PostMapping("/saveSocialUser")
    public String saveSocialUser(@RequestParam("provider")  String provider, @RequestParam("socialId")
    String socialId, @RequestParam("name")  String name, @RequestParam("username")  String username, @RequestParam("email")
                                 String email, @RequestParam("uuid")  String uuid, Model model) {
        Optional<SocialLoginInfo> socialLoginInfoOptional = socialLoginInfoService.findByProviderAndUuidAndSocialId(provider, uuid, socialId);

        if (socialLoginInfoOptional.isPresent()) {
            SocialLoginInfo socialLoginInfo = socialLoginInfoOptional.get();
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(socialLoginInfo.getCreatedAt(), now);

            if (duration.toMinutes() > 20) {
                return "redirect:/error"; // 20분 이상 경과한 경우 에러 페이지로 리다이렉트
            }

            // 유효한 경우 User 정보를 저장합니다.
            userService.saveUser(username, name, email, socialId, provider,passwordEncoder);
            return "redirect:/";
        } else {
            return "redirect:/error"; // 해당 정보가 없는 경우 에러 페이지로 리다이렉트
        }
    }
}
