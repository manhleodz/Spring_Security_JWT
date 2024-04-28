package com.manhleo.SpringSecurityJWT.dto;

import com.manhleo.SpringSecurityJWT.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String accessToken;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String phone;
    private String role;
    private String password;
    private Users User;
}
