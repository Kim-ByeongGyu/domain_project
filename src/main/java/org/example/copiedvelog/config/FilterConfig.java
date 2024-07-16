package org.example.copiedvelog.config;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.security.jwt.filter.JwtAuthenticationFilter;
import org.example.copiedvelog.security.jwt.util.JwtTokenizer;
import org.example.copiedvelog.service.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final JwtTokenizer jwtTokenizer;
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        JwtAuthenticationFilter jwtauthenticationFilter = new JwtAuthenticationFilter(jwtTokenizer);
        registrationBean.setFilter(jwtauthenticationFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
