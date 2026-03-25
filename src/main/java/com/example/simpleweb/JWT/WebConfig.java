package com.example.simpleweb.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 문지기가 지킬 경로 (방명록 작성 API)
                .addPathPatterns("/api/guestbook")

                // 문지기가 프리패스 시켜줄 경로들 (화면, 로그인, 회원가입, H2콘솔 등)
                .excludePathPatterns(
                        "/api/auth/**",
                        "/h2-console/**",
                        "/**/*.html",
                        "/favicon.ico",
                        "/error"
                );
    }
}