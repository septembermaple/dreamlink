package com.dreamlink.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dreams")
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 梦境ID，主键，自增

    @Column(nullable = false)
    private Long userId; // 用户ID，关联用户表，不能为空

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 梦境内容，使用TEXT类型存储，不能为空

    private boolean isPublic = false; // 是否公开，默认为false

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
