package com.manhleo.SpringSecurityJWT.repository;

import com.manhleo.SpringSecurityJWT.entity.Users;
import com.manhleo.SpringSecurityJWT.entity.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {
    Optional<Token> findByRefreshToken(String refreshToken);

    Optional<Token> findByUser(Users users);

    @Modifying
    int deleteByUser(Users user);
}
