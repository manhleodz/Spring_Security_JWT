package com.manhleo.SpringSecurityJWT.service;

import com.manhleo.SpringSecurityJWT.dto.LoginDto;
import com.manhleo.SpringSecurityJWT.dto.RegisterDto;
import com.manhleo.SpringSecurityJWT.dto.ReqRes;
import com.manhleo.SpringSecurityJWT.entity.ERole;
import com.manhleo.SpringSecurityJWT.entity.Role;
import com.manhleo.SpringSecurityJWT.entity.Token;
import com.manhleo.SpringSecurityJWT.entity.Users;
import com.manhleo.SpringSecurityJWT.repository.OurUserRepo;
import com.manhleo.SpringSecurityJWT.repository.RoleRepository;
import com.manhleo.SpringSecurityJWT.repository.TokenRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.*;

@Service
public class AuthService {

    @Data
    static
    class JwtPayload {
        int id;
        String phone;
        private Set<Role> roles = new HashSet<>();

    }

    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RoleRepository roleRepository;

    public ReqRes signUp(@Valid @RequestBody RegisterDto registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            Users ourUsers = new Users();
            ourUsers.setPhone(registrationRequest.getPhone());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            Set<String> strRoles = Collections.singleton(registrationRequest.getRole());
            Set<Role> roles = new HashSet<>();

            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ADMIN);
                    if (adminRole != null) roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.USER);
                    if (userRole != null) {
                        roles.add(userRole);
                    }
                }
            });
            ourUsers.setRoles(roles);

            Users ourUserResult = ourUserRepo.save(ourUsers);
            if (ourUserResult.getId() > 0) {
                resp.setUser(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signIn(@Valid @RequestBody LoginDto signInRequest) {
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getPhone(), signInRequest.getPassword()));
            var user = ourUserRepo.findByPhone(signInRequest.getPhone()).orElseThrow();
            var existedToken = refreshTokenService.findByUser(user);

            if (existedToken != null) {
                System.out.println("Token tồn tại");
                //token hết hạn
                if (!jwtUtils.isTokenValid(existedToken.getAccessToken(), user)) {
                    HashMap<String, Object> map = new HashMap<>();
                    JwtPayload jwtPayload = new JwtPayload();
                    jwtPayload.setId(user.getId());
                    jwtPayload.setRoles(user.getRoles());
                    map.put("user", jwtPayload);
                    System.out.println(map);
                    String jwt = jwtUtils.generateRefreshToken(map, user);
                    var refreshToken = refreshTokenService.updateRefreshToken(existedToken.getId(), jwt);

                    response.setStatusCode(200);
                    response.setUser(user);
                    response.setAccessToken(String.valueOf(jwt));
                    response.setRefreshToken(String.valueOf(refreshToken.getRefreshToken()));
                } else {
                    response.setStatusCode(200);
                    response.setUser(user);
                    response.setAccessToken(String.valueOf(existedToken.getAccessToken()));
                    response.setRefreshToken(String.valueOf(existedToken.getRefreshToken()));
                }
            } else {
                System.out.println("Token mới");
                HashMap<String, Object> map = new HashMap<>();
                JwtPayload jwtPayload = new JwtPayload();
                jwtPayload.setId(user.getId());
                jwtPayload.setRoles(user.getRoles());
                map.put("user", jwtPayload);
                String jwt = jwtUtils.generateRefreshToken(map, user);
                var refreshToken = refreshTokenService.createRefreshToken(user.getPhone(), jwt);
                response.setStatusCode(200);
                response.setUser(user);
                response.setAccessToken(String.valueOf(jwt));
                response.setRefreshToken(String.valueOf(refreshToken.getRefreshToken()));
            }
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest,String accessTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            accessTokenRequest = accessTokenRequest.substring(7);
            Token existedToken = refreshTokenService.findByRefreshToken(refreshTokenRequest.getRefreshToken());
            if (existedToken != null && accessTokenRequest.equals(existedToken.getAccessToken())) {
                Users user = ourUserRepo.findById(existedToken.getUser().getId()).get();
                HashMap<String, Object> map = new HashMap<>();
                JwtPayload jwtPayload = new JwtPayload();
                jwtPayload.setId(user.getId());
                jwtPayload.setRoles(user.getRoles());
                map.put("user", jwtPayload);
                var accessToken = jwtUtils.generateRefreshToken(map, user);
                var refreshToken = refreshTokenService.updateRefreshToken(existedToken.getId(), accessToken);

                response.setStatusCode(200);
                response.setUser(user);
                response.setAccessToken(String.valueOf(accessToken));
                response.setRefreshToken(String.valueOf(refreshToken.getRefreshToken()));
            } else {
                throw new Exception("Authorization failed!!");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}