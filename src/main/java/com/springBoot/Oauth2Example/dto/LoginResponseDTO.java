package com.springBoot.Oauth2Example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDTO {
    private String email;
    private Integer UserId;
    private String fullName;
    private String phoneNumber;
    private String role;
    private String authToken;
}
