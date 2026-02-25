package com.dreamlink.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 消息ID，主键，自增

    @Column(nullable = false)
    private String type; // 消息类型：chat(聊天), match(匹配), system(系统)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 消息内容，使用TEXT类型存储，不能为空

    @Column(nullable = false)
    private Long userId; // 消息所属用户ID，不能为空

    private Long otherUserId; // 其他用户ID，用于聊天消息

    private Long dreamId; // 梦境ID，用于匹配消息

    private Long otherDreamId; // 其他梦境ID，用于匹配消息

    private boolean isRead = false; // 是否已读，默认为false

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
