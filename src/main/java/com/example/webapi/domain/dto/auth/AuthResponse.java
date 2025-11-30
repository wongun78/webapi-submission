package com.example.webapi.domain.dto.auth;

import com.example.webapi.domain.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    @JsonProperty("access_token")
    private String accessToken;

    private UserResponse user;
}