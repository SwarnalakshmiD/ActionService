package com.example.UserActionService.model.entity;

import com.example.UserActionService.model.constant.FieldName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name=FieldName.SWIPE_HISTORY_T)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwipeHistory {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }





    public Time getSwipeTime() {
        return swipeTime;
    }

    public void setSwipeTime(Time swipeTime) {
        this.swipeTime = swipeTime;
    }

    @Id
    @Column(name=FieldName.ID)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name=FieldName.EMP_ID,nullable=false)
    private int employeeId;


    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name=FieldName.SWIPE_DATE,nullable=false)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String swipeDate;

    public String getSwipeDate() {
        return swipeDate;
    }

    public void setSwipeDate(String swipeDate) {
        this.swipeDate = swipeDate;
    }

    @Column(name=FieldName.SWIPE_TIME,nullable=false)
    private Time swipeTime;


}
