package com.example.simpleweb;

import com.example.simpleweb.JWT.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. 초간단 회원가입 (테스트용)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
        userRepository.save(user); // 비밀번호 평문 저장 (실무에선 절대 금지!)
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 2. 로그인 (Access & Refresh 토큰 발급)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserEntity loginUser) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(loginUser.getUsername());

        // 아이디와 비밀번호가 맞는지 확인
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginUser.getPassword())) {
            UserEntity user = userOpt.get();

            // 토큰 2개 생성
            String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

            // 리프레시 토큰은 DB에 저장 (나중에 검증하기 위해)
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            // 클라이언트에게 토큰 2개 전달
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return ResponseEntity.ok(tokens);
        }
        return ResponseEntity.status(401).body(null); // 로그인 실패
    }

    // 3. 토큰 재발급 (리프레시 토큰을 이용해 새 액세스 토큰 받기)
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        try {
            // 1. 전달받은 리프레시 토큰이 유효한지(만료안됐는지) 해독해봄
            String username = jwtUtil.extractUsername(refreshToken);
            Optional<UserEntity> userOpt = userRepository.findByUsername(username);

            // 2. DB에 저장된 유저의 리프레시 토큰과 클라이언트가 보낸 토큰이 일치하는지 확인
            if (userOpt.isPresent() && refreshToken.equals(userOpt.get().getRefreshToken())) {
                // 검증 완료! 새 액세스 토큰 발급
                String newAccessToken = jwtUtil.generateAccessToken(username);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", newAccessToken);
                return ResponseEntity.ok(tokens);
            }
        } catch (Exception e) {
            // 토큰이 위조되었거나 만료됨
        }
        return ResponseEntity.status(401).body(null);
    }
}