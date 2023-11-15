package com.example.forums_backend.service;


import com.example.forums_backend.dto.ReportRequestDto;
import com.example.forums_backend.dto.ReportResDto;
import com.example.forums_backend.entity.Report;
import com.example.forums_backend.entity.my_enum.ReportStatus;
import com.example.forums_backend.repository.ReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Report createReport(ReportRequestDto reportRequestDto) {
        // Tạo mới đối tượng ReportEntity với trạng thái mặc định là "Pending"
        Report reportEntity = new Report();
        // Các bước khác để thiết lập thông tin từ reportRequestDto

        return reportRepository.save(reportEntity); // Lưu vào cơ sở dữ liệu
    }

    private Report convertToEntity(ReportRequestDto reportRequestDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(reportRequestDto, Report.class);
    }

    @Override
    public void deleteReport(Long reportId) {
        reportRepository.deleteById(reportId);
    }

    @Override
    public List<ReportResDto> getAllReports() {
        List<Report> reportEntities = reportRepository.findAll();
        List<ReportResDto> reportDTOs = new ArrayList<>();

        for (Report reportEntity : reportEntities) {
            ReportResDto reportDTO = new ReportResDto();
            BeanUtils.copyProperties(reportEntity, reportDTO);
            reportDTOs.add(reportDTO);
        }

        return reportDTOs;
    }
    @Override
    public List<ReportResDto> getResolvedReports() {
        List<Report> resolvedReports = reportRepository.findByStatus(ReportStatus.RESOLVED);
        List<ReportResDto> resolvedReportDTOs = new ArrayList<>();

        for (Report reportEntity : resolvedReports) {
            ReportResDto reportDTO = new ReportResDto();
            BeanUtils.copyProperties(reportEntity, reportDTO);
            resolvedReportDTOs.add(reportDTO);
        }

        return resolvedReportDTOs;
    }
    @Override
    public List<ReportResDto> getPendingReports() {
        List<Report> pendingReports = reportRepository.findByStatus(ReportStatus.PENDING);
        List<ReportResDto> pendingReportDTOs = new ArrayList<>();

        for (Report reportEntity : pendingReports) {
            ReportResDto reportDTO = new ReportResDto();
            BeanUtils.copyProperties(reportEntity, reportDTO);
            pendingReportDTOs.add(reportDTO);
        }

        return pendingReportDTOs;
    }
    @Override
    public void updateReport(Long reportId, ReportRequestDto reportRequestDto) {
        // Kiểm tra xem report có tồn tại không
        Report existingReport = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));

        // Cập nhật thông tin từ reportRequestDto vào existingReport
        existingReport.setReportType(reportRequestDto.getReportType());
        existingReport.setReason(reportRequestDto.getReason());

        // Lưu cập nhật vào repository
        reportRepository.save(existingReport);
    }
    @Override
    public Report getReport(Long reportId) {
        // Thực hiện lấy report từ repository
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));
    }
}
