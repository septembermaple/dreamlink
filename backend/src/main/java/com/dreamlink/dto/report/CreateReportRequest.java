package com.dreamlink.dto.report;

import lombok.Data;

@Data
public class CreateReportRequest {
    private Long dreamId;
    private Long reporterId;
    private String reason;
    private String description;
}
