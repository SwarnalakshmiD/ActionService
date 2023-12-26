package com.example.UserActionService.dao.api;

import com.example.UserActionService.model.entity.Report;
import com.example.UserActionService.model.vo.ReportVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {


    void deleteByEmplId(int emplId);

    boolean existsByReportDateAndEmplId(String reportDate, int emplId);

    @Query(value = "SELECT * FROM Report r WHERE r.empl_id = :emplId AND r.report_date BETWEEN :startDate AND :endDate ORDER BY r.report_date", nativeQuery = true)
    List<Report> findReportByEmplIdAndReportDateRange(@Param("emplId") int emplId,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    @Query("SELECT r FROM Report r WHERE r.emplId = :emplId AND FUNCTION('MONTH', TO_DATE(r.reportDate, 'YYYY-MM-DD')) = :targetMonth")
    List<Report> findReportsByEmplIdAndTargetMonth(@Param("emplId") int emplId, @Param("targetMonth") int targetMonth);
}
