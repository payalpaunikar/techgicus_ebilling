package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.RefreshToken;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.User;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class RefreshTokenService {


    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(User user,String token){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(token);
        refreshToken.setExpirationDateTime(LocalDateTime.ofInstant(Instant.now().plusMillis(refreshTokenDurationMs), ZoneId.systemDefault()));

        refreshTokenRepository.save(refreshToken);
    }
}
