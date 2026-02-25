package com.dreamlink.dto.dream;

import lombok.Data;

@Data
public class UpdateDreamRequest {
    private String content;
    private boolean isPublic;
}
