package com.example.simpleweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guestbook")
public class GuestbookController {

    @Autowired
    private GuestbookRepository repository;

    // 1. 방명록 목록 조회 (GET)
    @GetMapping
    public List<GuestbookEntity> getAllMessages() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    // 2. 방명록 작성 (POST)
    @PostMapping
    public GuestbookEntity createMessage(@RequestBody GuestbookEntity entry) {
        // 간단한 검증
        if (entry.getName() == null || entry.getName().isEmpty() ||
                entry.getMessage() == null || entry.getMessage().isEmpty()) {
            throw new RuntimeException("이름과 내용을 입력해주세요.");
        }
        return repository.save(entry);
    }
}