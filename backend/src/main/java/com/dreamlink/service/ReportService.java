package com.dreamlink.service;

import com.dreamlink.dto.report.CreateReportRequest;
import com.dreamlink.dto.report.UpdateReportRequest;
import com.dreamlink.entity.Report;

import java.util.List;

public interface ReportService {

    Report createReport(CreateReportRequest request);

    List<Report> getAllReports();

    Report getReportDetail(Long reportId);

    Report updateReportStatus(Long reportId, UpdateReportRequest request);

}
