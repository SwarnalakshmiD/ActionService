package com.example.UserActionService.dao.api;

import com.example.UserActionService.model.entity.SwipeHistory;
import com.example.UserActionService.model.vo.SwipeHistoryVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface UserActionRepository extends JpaRepository<SwipeHistory, Long> {


   List<SwipeHistory> findAllByEmployeeId(int empId);


   @Query("SELECT employeeId, swipeDate, COUNT(*), MIN(swipeTime), MAX(swipeTime) " +
           "FROM SwipeHistory " +
           "WHERE employeeId = :employeeId " +
           "GROUP BY swipeDate, employeeId")
   List<Object[]> getSwipeSummaryByEmployeeId(@Param("employeeId") int employeeId);
  }


