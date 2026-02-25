package com.dreamlink.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String phone;
    private String constellation;
    private String nickname;
}
