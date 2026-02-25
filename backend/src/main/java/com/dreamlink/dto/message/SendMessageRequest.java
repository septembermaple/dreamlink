package com.dreamlink.dto.message;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String content;
    private Long userId;
    private Long otherUserId;
}