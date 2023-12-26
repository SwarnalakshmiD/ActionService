package com.example.UserActionService.serviceimplemetation;

import com.example.UserActionService.dao.api.OperationRepository;
import com.example.UserActionService.dao.api.ReportRepository;
import com.example.UserActionService.dao.api.UserActionRepository;
import com.example.UserActionService.model.entity.Operations;
import com.example.UserActionService.model.entity.Report;
import com.example.UserActionService.model.entity.SwipeHistory;
import com.example.UserActionService.model.vo.*;
import com.example.UserActionService.services.ActionServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service("actionServices")
public class ActionServiceImplementation implements ActionServices {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserActionRepository userActionRepository;

    @Autowired
    OperationRepository operationRepository;

    @Autowired
    ReportRepository reportRepository;

    private String userServiceUrl="http://localhost:8080/userService";
    private String notificationUrl="http://localhost:8099/notification";
    SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public ActionServiceImplementation(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(NotificationVo notificationVo) throws JsonProcessingException {
        System.out.println("--------------------------------- inside approve  producer");
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send("com.quinbay.product.create",   objectMapper.writeValueAsString(notificationVo));
    }

    public List<SwipeHistory> getUserSwipehistory(int id) {

        List<SwipeHistory> swipeHistories=userActionRepository.findAllByEmployeeId(id);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));

//        List<SwipeHistoryVo> list = objectMapper.convertValue(swipeHistories, List.class);
        return swipeHistories;

    }


    public String applyUserActions(int id, OperationsVo operation)
    {
        operation.setEmpId(id);
        operation.setCreatedDate(Date.valueOf(LocalDate.now()).toString());
        operation.setStatus("pending");
        operation.setStatusUpdatedDate(Date.valueOf(LocalDate.now()).toString());
        ObjectMapper objectMapper = new ObjectMapper();
        operationRepository.save(objectMapper.convertValue(operation,Operations.class));
        return operation.getActionType()+" applied";

    }

    public String applyActionStatus( Operations operationDetails)
    {
        Operations operation= operationRepository.findByEmpIdAndActionTypeAndCreatedDateAndActionStarted(
                operationDetails.getEmpId(), operationDetails.getActionType(), operationDetails.getCreatedDate(), operationDetails.getActionStarted());

        operation.setStatus(operationDetails.getStatus());

        operation.setStatusUpdatedDate(Date.valueOf(LocalDate.now()).toString());
        operationRepository.save(operation);
        NotificationVo notification =new NotificationVo();
        notification.empId=operation.getEmpId();
        notification.notificationMessage=operation.getStatus();
        notification.readStatus="unread";
        notification.actionType=operation.getActionType();
        notification.actionStartDate=operation.getActionStarted();
        notification.updatedDate=operation.getStatusUpdatedDate();
        try{
            sendMessage(notification);
        }catch (JsonProcessingException exception){
            System.out.println(exception);
        }

        return "status updated";

    }

    public String userSwipeDetails(SwipeHistoryVo swipeHistory)
    {
        if (swipeHistory.getSwipeTime() == null || swipeHistory.getSwipeDate() == null) {

            if (swipeHistory.getSwipeDate() == null) {
                swipeHistory.setSwipeDate(Date.valueOf(LocalDate.now()).toString());
            }
            if (swipeHistory.getSwipeTime() == null) {
                swipeHistory.setSwipeTime(Time.valueOf(LocalTime.now()));
            }
        }
        ObjectMapper objectMapper=new ObjectMapper();

        userActionRepository.save(objectMapper.convertValue(swipeHistory,SwipeHistory.class));
        return "Success";


    }

    public List<OperationsVo> getUserActionhistory(int id)
    {
        List<Operations> operation= operationRepository.findAllByEmpId(id);
        ObjectMapper objectMapper = new ObjectMapper();

        List<OperationsVo> list = objectMapper.convertValue(operation, List.class);
        return list;
    }


   public List<OperationsVo> viewEmployeePendingStatus(int id)
   {
       System.out.println("hi"+id);
       HttpHeaders headers = new HttpHeaders();
       headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       HttpEntity<String> entity = new HttpEntity<>(headers);
       UriComponents builder = UriComponentsBuilder.fromHttpUrl(userServiceUrl+"/user/manager").queryParam("managerId",id).build();
       List<UserVo> employees=restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,new ParameterizedTypeReference<List<UserVo>>(){}).getBody();
       List<Integer> empIds = new ArrayList<>();
       for (UserVo user : employees) {
           empIds.add(user.getId());
       }
       ObjectMapper objectMapper = new ObjectMapper();
       List<OperationsVo> empoloyeeStatus=objectMapper.convertValue(operationRepository.findPendingActionsByEmployeeIds(empIds),new TypeReference<List<OperationsVo>>() {});
       List<OperationsVo> employeeList = new ArrayList<>();
       for (OperationsVo operation : empoloyeeStatus) {
           for (UserVo employee : employees) {
               if (operation.getEmpId() == employee.getId()) {
                   operation.setEmployeeName(employee.getEmployeeName());
                   break;
               }
           }
           employeeList.add(operation);
       }
       return  employeeList;
   }

    public void report(int id) {
        List<Object[]> entries = userActionRepository.getSwipeSummaryByEmployeeId(id);
        List<Object[]> entriesForActions = operationRepository.findActionSummaryByEmpId(id);

        for (Object[] row : entries) {
            int eid = (int) row[0];

            String swipeDate = (String) row[1];

            Time min = (Time) row[3];
            Time max = (Time) row[4];
            Report report = new Report();
            report.setEmplId(eid);
            report.setCheckIn(min);
            report.setCheckOut(max);
            report.setReportDate(swipeDate);
            int diff = max.getHours() - min.getHours();
            report.setWorkingHours(diff);
            if (!reportRepository.existsByReportDateAndEmplId(report.reportDate, id))
                reportRepository.save(report);
        }

        for (Object[] row : entriesForActions) {
            int eid = (int) row[0];
            String actionType = (String) row[1];
            String startDateString = (String) row[2];
            String endDateString = (String) row[3];
            LocalDate startDate = LocalDate.parse(startDateString);
            LocalDate endDate = LocalDate.parse(endDateString);
            while (!startDate.isAfter(endDate)) {

                Report report = new Report();
                report.setEmplId(eid);
                report.setActionType(actionType);
                report.setReportDate(startDateString);
                if (!reportRepository.existsByReportDateAndEmplId(report.reportDate, id))
                    reportRepository.save(report);

                startDate = startDate.plusDays(1);
            }

        }
    }

        public List<ReportVo> reportGenerationForWeek(ReportVo reportVoReqBody) {
            report(reportVoReqBody.emplId);
            List<Report> reportListById = reportRepository.findReportByEmplIdAndReportDateRange(reportVoReqBody.emplId, reportVoReqBody.getStartDate(), reportVoReqBody.getEndDate());
            return reportListById.stream()

                    .map(report -> {
                        ReportVo rVo = new ReportVo();
                        rVo.setReportId(report.getReportId());
                        rVo.setEmplId(report.getEmplId());
                        rVo.setReportDate(report.getReportDate());
                        rVo.setCheckIn(report.getCheckIn());
                        rVo.setCheckOut(report.getCheckOut());
                        rVo.setWorkingHours(report.getWorkingHours());
                        rVo.setActionType(report.getActionType());
                        LocalDate reportLocalDate = LocalDate.parse(report.getReportDate());
                        rVo.setDay(reportLocalDate.getDayOfWeek().toString());

                        return rVo;
                    })
                    .collect(Collectors.toList());
        }

    public List<ReportVo> reportGenerationForMonth(ReportVo reportVoReqBody) {
        report(reportVoReqBody.emplId);
        int targetMonth = Month.valueOf(reportVoReqBody.startDate.toUpperCase()).getValue();
        List<Report> reportListById = reportRepository.findReportsByEmplIdAndTargetMonth(reportVoReqBody.emplId, targetMonth);
        return reportListById.stream()

                .map(report -> {
                    ReportVo rVo = new ReportVo();
                    rVo.setReportId(report.getReportId());
                    rVo.setEmplId(report.getEmplId());
                    rVo.setReportDate(report.getReportDate());
                    rVo.setCheckIn(report.getCheckIn());
                    rVo.setCheckOut(report.getCheckOut());
                    rVo.setWorkingHours(report.getWorkingHours());
                    rVo.setActionType(report.getActionType());
                    LocalDate reportLocalDate = LocalDate.parse(report.getReportDate());
                    rVo.setDay(reportLocalDate.getDayOfWeek().toString());

                    return rVo;
                })
                .collect(Collectors.toList());
    }


    }


