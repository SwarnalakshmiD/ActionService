package com.example.UserActionService.model.entity;

import com.example.UserActionService.model.constant.FieldName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name=FieldName.REPORT_T)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @Column(name=FieldName.REPORT_ID)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int reportId;

    @Column(name=FieldName.EMPL_ID)
    public int emplId;

    @Column(name=FieldName.REPORT_DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public String reportDate;

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    @Column(name=FieldName.CHECK_IN)
    public Time checkIn;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    @Column(name=FieldName.CHECK_OUT)
    public Time checkOut;

    @Column(name=FieldName.WORKING_HOURS)
    public int workingHours;

    @Column(name=FieldName.ACTION_TYPE_REPORT)
    public String actionType;




    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getEmplId() {
        return emplId;
    }

    public void setEmplId(int emplId) {
        this.emplId = emplId;
    }


    public Time getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Time checkIn) {
        this.checkIn = checkIn;
    }

    public Time getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Time checkOut) {
        this.checkOut = checkOut;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int  workingHours) {
        this.workingHours = workingHours;
    }
}
