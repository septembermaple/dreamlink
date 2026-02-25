package com.dreamlink.controller;

import com.dreamlink.dto.ApiResponse;
import com.dreamlink.dto.report.CreateReportRequest;
import com.dreamlink.dto.report.UpdateReportRequest;
import com.dreamlink.entity.Report;
import com.dreamlink.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ApiResponse<?> createReport(@RequestBody CreateReportRequest request) {
        try {
            Report report = reportService.createReport(request);
            return ApiResponse.success(report);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<?> getAllReports() {
        try {
            List<Report> reports = reportService.getAllReports();
            return ApiResponse.success(reports);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{reportId}")
    public ApiResponse<?> getReportDetail(@PathVariable Long reportId) {
        try {
            Report report = reportService.getReportDetail(reportId);
            return ApiResponse.success(report);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{reportId}")
    public ApiResponse<?> updateReportStatus(@PathVariable Long reportId, @RequestBody UpdateReportRequest request) {
        try {
            Report report = reportService.updateReportStatus(reportId, request);
            return ApiResponse.success(report);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

}
