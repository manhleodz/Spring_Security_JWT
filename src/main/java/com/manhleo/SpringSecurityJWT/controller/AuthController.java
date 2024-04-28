package com.manhleo.SpringSecurityJWT.controller;

import com.manhleo.SpringSecurityJWT.dto.RegisterDto;
import com.manhleo.SpringSecurityJWT.service.AuthService;
import com.manhleo.SpringSecurityJWT.dto.LoginDto;
import com.manhleo.SpringSecurityJWT.dto.ReqRes;
import com.manhleo.SpringSecurityJWT.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody RegisterDto signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody LoginDto signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(
            @RequestBody ReqRes refreshTokenRequest,
            @RequestHeader("Authorization") String accessTokenRequest
    ) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest, accessTokenRequest));
    }
}
