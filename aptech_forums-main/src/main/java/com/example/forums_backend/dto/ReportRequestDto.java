package com.example.forums_backend.dto;

import com.example.forums_backend.entity.my_enum.ReportType;

//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ReportRequestDto {
//    private Post post;
//    private String detail;
//    private int type;
//    private Report.Status status; // Thêm trường status với kiểu dữ liệu enum tương tự trong entity
//
//    public Report.Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Report.Status status) {
//        this.status = status;
//    }
//}
// dto/ReportRequestDto.java
public class ReportRequestDto {
    private Long postId;
    private Long accountId;
    private ReportType reportType;
    private String reason;

    // Getters and setters
    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
