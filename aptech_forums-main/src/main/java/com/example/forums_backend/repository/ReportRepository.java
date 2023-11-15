package com.example.forums_backend.repository;

import com.example.forums_backend.dto.ReportResDto;
import com.example.forums_backend.entity.Report;
import com.example.forums_backend.entity.my_enum.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // Các phương thức tùy chỉnh nếu cần
        List<Report> findByStatus(ReportStatus status);
//    @Query("SELECT r FROM Report r WHERE r.created_at >= :startDate AND r.created_at <= :endDate")
//    List<Report> findAllReportsBetweenDates(
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );
    @Query("SELECT r FROM Report r ORDER BY r.createdAt DESC")
    List<ReportResDto> findAllReports();
    @Query("SELECT r FROM Report r WHERE r.status = 'PENDING' ORDER BY r.createdAt DESC")
    List<ReportResDto> findPendingReports();

}
