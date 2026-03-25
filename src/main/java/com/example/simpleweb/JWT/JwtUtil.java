package com.example.simpleweb.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 토큰을 암호화할 때 쓰는 비밀키 (실제 서비스에서는 환경변수로 숨겨야 합니다)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Access Token 만료 시간: 30분
    private final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 30;
    // Refresh Token 만료 시간: 7일
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;

    // 1. Access Token 생성
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    // 2. Refresh Token 생성
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    // 3. 토큰에서 유저 이름(username) 꺼내기 및 위조 검증
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}