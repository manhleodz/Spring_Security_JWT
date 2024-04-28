package com.manhleo.SpringSecurityJWT.repository;

import com.manhleo.SpringSecurityJWT.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OurUserRepo extends JpaRepository<Users, Integer> {
    Optional<Users> findByPhone(String phone);
}
