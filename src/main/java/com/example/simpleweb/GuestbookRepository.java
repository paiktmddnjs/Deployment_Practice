package com.example.simpleweb;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuestbookRepository extends JpaRepository<GuestbookEntity, Long> {
    // 모든 방명록을 최신순으로 가져오기
    List<GuestbookEntity> findAllByOrderByCreatedAtDesc();
}