package com.dreamlink.service.impl;

import com.dreamlink.dto.auth.LoginRequest;
import com.dreamlink.dto.auth.LoginResponse;
import com.dreamlink.dto.auth.RegisterRequest;
import com.dreamlink.dto.auth.UpdateUserRequest;
import com.dreamlink.entity.User;
import com.dreamlink.repository.UserRepository;
import com.dreamlink.service.AuthService;
import com.dreamlink.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsernameOrPhone(request.getUsername(), request.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("用户名或手机号不存在");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId());
        return new LoginResponse(token, user);
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否已存在
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("手机号已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname() : request.getUsername());
        user.setPhone(request.getPhone());
        user.setConstellation(request.getConstellation());
        user.setAvatar("https://api.dicebear.com/9.x/avataaars/svg?seed=" + request.getUsername());
        user.setAdmin(false);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId());
        return new LoginResponse(token, user);
    }

    @Override
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    public User updateUserInfo(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (StringUtils.hasText(request.getNickname())) {
            user.setNickname(request.getNickname());
        }
        if (StringUtils.hasText(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }
        if (StringUtils.hasText(request.getConstellation())) {
            user.setConstellation(request.getConstellation());
        }
        if (StringUtils.hasText(request.getPhone())) {
            // 检查手机号是否已被其他用户使用
            Optional<User> existingUser = userRepository.findByPhone(request.getPhone());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new RuntimeException("手机号已存在");
            }
            user.setPhone(request.getPhone());
        }

        return userRepository.save(user);
    }

}
