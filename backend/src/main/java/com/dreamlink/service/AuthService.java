package com.dreamlink.service;

import com.dreamlink.dto.auth.LoginRequest;
import com.dreamlink.dto.auth.LoginResponse;
import com.dreamlink.dto.auth.RegisterRequest;
import com.dreamlink.dto.auth.UpdateUserRequest;
import com.dreamlink.entity.User;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse register(RegisterRequest request);

    User getUserInfo(Long userId);

    User updateUserInfo(Long userId, UpdateUserRequest request);

}
