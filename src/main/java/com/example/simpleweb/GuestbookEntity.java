package com.example.simpleweb;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guestbook")
public class GuestbookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String message;

    private LocalDateTime createdAt;

    // Getter, Setter
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @PrePersist // DB 저장 전 시간 설정
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}