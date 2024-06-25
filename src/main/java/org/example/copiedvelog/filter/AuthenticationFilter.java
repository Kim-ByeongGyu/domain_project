package org.example.copiedvelog.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.copiedvelog.config.UserContext;
import org.example.copiedvelog.entity.User;

import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String auth = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("auth")) {
                        auth = cookie.getValue();
                        break;
                    }
                }
            }
            if (auth != null) {
                User user = new User();
                user.setUsername(auth);

                UserContext.setUser(user);
            }

            filterChain.doFilter(request, servletResponse);
        } finally {
            UserContext.clear();
        }
    }
}
