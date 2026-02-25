package com.dreamlink.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 用户ID，主键，自增

    @Column(unique = true, nullable = false)
    private String username; // 用户名，唯一，不能为空

    @Column(nullable = false)
    private String password; // 密码，不能为空，存储加密后的密码

    private String nickname; // 昵称，默认为用户名

    private String avatar; // 头像URL

    private String constellation; // 星座

    @Column(unique = true)
    private String phone; // 手机号，唯一

    private boolean isAdmin = false; // 是否为管理员，默认为false

    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 更新时间

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
