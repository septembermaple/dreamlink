package com.dreamlink.controller;

import com.dreamlink.dto.ApiResponse;
import com.dreamlink.dto.auth.LoginRequest;
import com.dreamlink.dto.auth.RegisterRequest;
import com.dreamlink.dto.auth.UpdateUserRequest;
import com.dreamlink.entity.User;
import com.dreamlink.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        try {
            return ApiResponse.success(authService.login(request));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest request) {
        try {
            return ApiResponse.success(authService.register(request));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<?> getUserInfo(@PathVariable Long userId) {
        try {
            User user = authService.getUserInfo(userId);
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/user/{userId}")
    public ApiResponse<?> updateUserInfo(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        try {
            User user = authService.updateUserInfo(userId, request);
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

}
