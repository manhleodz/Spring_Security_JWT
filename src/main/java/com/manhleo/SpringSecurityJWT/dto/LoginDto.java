package com.manhleo.SpringSecurityJWT.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto {
    @NotBlank
    private String phone;

    @NotBlank
    private String password;
}
