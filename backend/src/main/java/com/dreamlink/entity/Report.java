package com.dreamlink.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 举报ID，主键，自增

    @Column(nullable = false)
    private Long dreamId; // 被举报的梦境ID，不能为空

    @Column(nullable = false)
    private Long reporterId; // 举报人ID，不能为空

    @Column(nullable = false)
    private String reason; // 举报原因，不能为空

    @Column(columnDefinition = "TEXT")
    private String description; // 详细描述，使用TEXT类型存储

    @Column(nullable = false)
    private String status = "pending"; // 举报状态：pending(待处理), processed(已处理), dismissed(已驳回)

    private LocalDateTime processedAt; // 处理时间

    private Long processedBy; // 处理人ID

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
