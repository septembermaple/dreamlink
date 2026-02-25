package com.dreamlink.dto.comment;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private Long dreamId;
    private Long userId;
    private String content;
}
