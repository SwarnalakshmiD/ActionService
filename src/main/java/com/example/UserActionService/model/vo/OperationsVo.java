package com.example.UserActionService.model.vo;

import java.sql.Date;


public class OperationsVo {
    private int actionId;

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatusUpdatedDate() {
        return statusUpdatedDate;
    }

    public void setStatusUpdatedDate(String statusUpdatedDate) {
        this.statusUpdatedDate = statusUpdatedDate;
    }

    public String getActionStarted() {
        return actionStarted;
    }

    public void setActionStarted(String actionStarted) {
        this.actionStarted = actionStarted;
    }

    public String getActionEnded() {
        return actionEnded;
    }

    public void setActionEnded(String actionEnded) {
        this.actionEnded = actionEnded;
    }

    private int empId;
    private String status;
    private String actionType;
    private String createdDate;


    private String statusUpdatedDate;
    private String actionStarted;
    private String actionEnded;


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    private String employeeName;
}
