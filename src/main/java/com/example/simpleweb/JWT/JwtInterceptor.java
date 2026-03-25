package com.example.simpleweb.JWT;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 헤더에서 Authorization 값을 꺼냅니다.
        String authHeader = request.getHeader("Authorization");

        // 2. 토큰이 'Bearer '로 시작하는지 확인합니다.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // "Bearer " 글자(7칸)를 떼어내고 진짜 토큰만 추출
            String token = authHeader.substring(7);
            try {
                // 3. 토큰 해독 및 위조/만료 검사 (문제가 있으면 여기서 에러가 터짐)
                String username = jwtUtil.extractUsername(token);

                // (선택) 컨트롤러에서 유저 이름을 바로 쓸 수 있게 요청(request) 객체에 담아줍니다.
                request.setAttribute("username", username);

                return true; // 무사히 통과! 컨트롤러로 이동
            } catch (Exception e) {
                // 토큰 만료 또는 위조된 경우
                System.out.println("토큰 검증 실패: " + e.getMessage());
            }
        }

        // 4. 토큰이 없거나 검증에 실패하면 401(Unauthorized) 에러를 튕겨냅니다.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false; // 컨트롤러로 접근을 막음
    }
}