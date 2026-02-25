package com.dreamlink.dto.report;

import lombok.Data;

@Data
public class UpdateReportRequest {
    private String status; // pending, processed, dismissed
    private Long processedBy;
}
