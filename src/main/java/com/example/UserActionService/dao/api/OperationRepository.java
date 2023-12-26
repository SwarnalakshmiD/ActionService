package com.example.UserActionService.dao.api;

import com.example.UserActionService.model.entity.Operations;
import com.example.UserActionService.model.entity.SwipeHistory;
import com.example.UserActionService.model.vo.OperationsVo;
import com.example.UserActionService.model.vo.SwipeHistoryVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operations, Long> {
    Operations findByEmpIdAndActionType(int empId, String actionType);
    List<Operations> findAllByEmpId(int empId);

    @Query("SELECT a FROM Operations a WHERE a.empId IN :empIds AND a.status = 'pending'")
     List<Operations> findPendingActionsByEmployeeIds(@Param("empIds") List<Integer> empIds);

    Operations findByEmpIdAndActionTypeAndCreatedDateAndActionStarted(
            int empId, String actionType, String createdDate, String actionStarted);

    @Query("SELECT o.empId,o.actionType, o.actionStarted, o.actionEnded " +
            "FROM Operations o " +
            "WHERE o.empId = :empId " +
            "GROUP BY o.empId, o.actionType, o.actionStarted, o.actionEnded")
    List<Object[]> findActionSummaryByEmpId(@Param("empId") int empId);

}
