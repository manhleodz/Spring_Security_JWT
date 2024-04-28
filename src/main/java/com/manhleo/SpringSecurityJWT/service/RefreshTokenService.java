package com.manhleo.SpringSecurityJWT.service;

import com.manhleo.SpringSecurityJWT.entity.Token;
import com.manhleo.SpringSecurityJWT.entity.Users;
import com.manhleo.SpringSecurityJWT.repository.OurUserRepo;
import com.manhleo.SpringSecurityJWT.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    OurUserRepo ourUserRepo;

    public Optional<Token> findById(Integer id) {
        return tokenRepository.findById(id);
    }

    public Token createRefreshToken(String phone, String jwtToken) {
        Token token = Token.builder()
                .user(ourUserRepo.findByPhone(phone).get())
                .refreshToken(UUID.randomUUID().toString())
                .accessToken(jwtToken)
                .expiryDate(Instant.now().plusMillis(1200000)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file
                .build();

        return  tokenRepository.save(token);
    }

    public Token updateRefreshToken(Integer id, String accessToken) {
        String newRefreshToken = UUID.randomUUID().toString();
        System.out.println("update token");
        Optional<Token> newRow = tokenRepository.findById(id)
                .map(token -> {
                    token.setRefreshToken(newRefreshToken);
                    token.setAccessToken(accessToken);
                    token.setExpiryDate(Instant.now().plusMillis(1200000));
                    return tokenRepository.save(token);
                });
        return newRow.orElse(null);
    }

    public Token findByRefreshToken(String token) {
        return tokenRepository.findByRefreshToken(token).get();
    }

    public Token findByUser(Users user) {
        return tokenRepository.findByUser(user).get();
    }

    public Token verifyExpiration(Token token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new RuntimeException(token.getAccessToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
