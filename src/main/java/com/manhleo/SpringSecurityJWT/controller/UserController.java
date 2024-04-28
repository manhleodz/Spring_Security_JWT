package com.manhleo.SpringSecurityJWT.controller;

import com.manhleo.SpringSecurityJWT.entity.Users;
import com.manhleo.SpringSecurityJWT.repository.OurUserRepo;
import com.manhleo.SpringSecurityJWT.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    private AuthService authService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Users> findAll() {
        return ourUserRepo.findAll();
    }
}
