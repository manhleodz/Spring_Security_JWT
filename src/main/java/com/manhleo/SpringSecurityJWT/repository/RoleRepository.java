package com.manhleo.SpringSecurityJWT.repository;

import com.manhleo.SpringSecurityJWT.entity.Role;
import com.manhleo.SpringSecurityJWT.entity.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(ERole name);
}