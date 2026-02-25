package com.dreamlink.service.impl;

import com.dreamlink.dto.report.CreateReportRequest;
import com.dreamlink.dto.report.UpdateReportRequest;
import com.dreamlink.entity.Report;
import com.dreamlink.repository.ReportRepository;
import com.dreamlink.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Report createReport(CreateReportRequest request) {
        Report report = new Report();
        report.setDreamId(request.getDreamId());
        report.setReporterId(request.getReporterId());
        report.setReason(request.getReason());
        report.setDescription(request.getDescription());
        report.setStatus("pending");

        return reportRepository.save(report);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Report getReportDetail(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("举报不存在"));
    }

    @Override
    public Report updateReportStatus(Long reportId, UpdateReportRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("举报不存在"));

        report.setStatus(request.getStatus());
        report.setProcessedBy(request.getProcessedBy());
        report.setProcessedAt(LocalDateTime.now());

        return reportRepository.save(report);
    }

}
