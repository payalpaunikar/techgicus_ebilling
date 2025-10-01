package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long refreshTokenId;


     @Column(nullable = false)
     private String refreshToken;

     @Column(nullable = false)
     private LocalDateTime expirationDateTime;


     @ManyToOne
     @JoinColumn(name = "user_id",nullable = false)
     private User user;

    public RefreshToken() {
    }

    public RefreshToken(Long refreshTokenId, String refreshToken, LocalDateTime expirationDateTime, User user) {
        this.refreshTokenId = refreshTokenId;
        this.refreshToken = refreshToken;
        this.expirationDateTime = expirationDateTime;
        this.user = user;
    }

    public Long getRefreshTokenId() {
        return refreshTokenId;
    }

    public void setRefreshTokenId(Long refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
