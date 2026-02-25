package com.dreamlink.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username; // 用户名或手机号
    private String password;
}
