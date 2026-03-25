package com.example.simpleweb;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 아이디로 회원 정보 찾기
    Optional<UserEntity> findByUsername(String username);
}