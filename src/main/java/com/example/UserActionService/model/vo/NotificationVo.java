package com.example.UserActionService.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationVo {
    public int empId;
    public String notificationMessage;
    public String readStatus;
   public String actionType;
    public String actionStartDate;
    public String updatedDate;
}
