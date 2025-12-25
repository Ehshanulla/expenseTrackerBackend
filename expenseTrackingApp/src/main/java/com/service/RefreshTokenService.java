package com.service;

import com.entities.RefreshToken;
import com.entities.User;
import com.repositories.RefreshTokenRepository;
import com.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username){
        User userInfoExtracted = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found!!"));
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))//have to add an environment variable here
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
