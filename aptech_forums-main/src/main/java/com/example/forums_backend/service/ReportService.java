package com.example.forums_backend.service;

import com.example.forums_backend.dto.ReportRequestDto;
import com.example.forums_backend.dto.ReportResDto;
import com.example.forums_backend.entity.Report;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// service/ReportService.java


public interface ReportService {
    Report createReport(ReportRequestDto reportRequestDto);
    void deleteReport(Long reportId);
    List<ReportResDto> getAllReports();
    List<ReportResDto> getResolvedReports();
    List<ReportResDto> getPendingReports();
    void updateReport(Long reportId, ReportRequestDto reportRequestDto);
    Report getReport(Long reportId);

}

//public class ReportService {
//    @Autowired
//    private ReportRepository reportRepository;

//    public List<Report> getAllReports() {
//        return reportRepository.findAll();
//    }


//    public List<Object[]> getAllReportIdsAndTypesByCreationDate() {
//        return reportRepository.findReportIdsAndTypesByCreationDate();
//    }
//    public ReportResDto fromEntityReportDto(Report report) {
//        ReportResDto reportResDto = new ReportResDto();
//        reportResDto.setId(report.getId());
//        reportResDto.setPostId(report.getPost().getId());
//        reportResDto.setDetail(report.getDetail());
//        reportResDto.setType(report.getType());
//        reportResDto.setProcessed(report.getStatus() == Report.Status.PROCESSED);
//        reportResDto.setReportTime(report.getCreatedAt());
//
//        return reportResDto;
//    }
//
//    public Report getReportById(Long id) {
//        Optional<Report> reportOptional = reportRepository.findById(id);
//        return reportOptional.orElse(null);
//    }
//
//    public List<Object[]> getReportIdsTypesStatusByCreationDate() {
//        return reportRepository.findReportIdsTypesStatusByCreationDate();
//    }
//
//    public Report createReport(Report report) {
//        return reportRepository.save(report);
//    }
//    public Report createReport(ReportRequestDto reportRequestDto) {
//        Report report = new Report();
//        report.setPost(reportRequestDto.getPost());
//        report.setDetail(reportRequestDto.getDetail());
//        report.setType(reportRequestDto.getType());
//        report.setStatus(reportRequestDto.getStatus()); // Ánh xạ trạng thái từ ReportRequestDto sang Report
//        // Thực hiện lưu báo cáo và trả về
//        return reportRepository.save(report);
//    }
//
//    public ReportResDto updateReportStatus(Long id, ReportRequestDto reportRequestDto) throws AppException {
//        Report existingReport = getReportById(id);
//
//        // Kiểm tra xem báo cáo đã được tìm thấy hay chưa.
//        if (existingReport == null) {
//            throw new AppException("REPORT NOT FOUND");
//        }
//
//        // Cập nhật trạng thái báo cáo từ dữ liệu trong reportRequestDto.
//        existingReport.setStatus(reportRequestDto.getStatus());
//
//        // Lưu báo cáo đã được cập nhật vào cơ sở dữ liệu.
//        reportRepository.save(existingReport);
//
//        // Chuyển đổi báo cáo đã cập nhật thành đối tượng ReportResDto và trả về cho người dùng.
//        return fromEntityReportDto(existingReport);
//    }
//
//    public void deleteReport(Long id) {
//        reportRepository.deleteById(id);
//    }
//    public List<ReportResDto> getAllReports() {
//        List<Report> reports = reportRepository.findAll();
//        List<ReportResDto> reportDtos = new ArrayList<>();
//
//        for (Report report : reports) {
//            reportDtos.add(fromEntityReportDto(report));
//        }
//
//        return reportDtos;
//    }
//}
