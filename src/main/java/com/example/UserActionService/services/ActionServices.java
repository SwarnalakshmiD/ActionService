package com.example.UserActionService.services;

import com.example.UserActionService.model.entity.Operations;
import com.example.UserActionService.model.entity.Report;
import com.example.UserActionService.model.entity.SwipeHistory;
import com.example.UserActionService.model.vo.OperationsVo;
import com.example.UserActionService.model.vo.ReportVo;
import com.example.UserActionService.model.vo.SwipeHistoryVo;
import com.example.UserActionService.model.vo.UserVo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ActionServices {
    List<SwipeHistory> getUserSwipehistory(int id);

    String applyUserActions(int id, OperationsVo operations);

    String applyActionStatus(Operations operation);

    String userSwipeDetails(SwipeHistoryVo swipeHistory);

    List<OperationsVo> getUserActionhistory(int id);

    List<OperationsVo> viewEmployeePendingStatus(int id);

    List<ReportVo> reportGenerationForWeek(ReportVo reportVoReqBody);
    List<ReportVo> reportGenerationForMonth(ReportVo reportVoReqBody);


}
