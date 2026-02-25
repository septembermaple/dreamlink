package com.dreamlink.dto.dream;

import lombok.Data;

@Data
public class CreateDreamRequest {
    private Long userId;
    private String content;
    private boolean isPublic = false;
}
