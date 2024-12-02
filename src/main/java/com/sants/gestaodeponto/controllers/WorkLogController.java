package com.sants.gestaodeponto.controllers;

import com.sants.gestaodeponto.domain.worklog.RequestWorkLogDTO;
import com.sants.gestaodeponto.domain.worklog.ResponseWorkLogDTO;
import com.sants.gestaodeponto.domain.worklog.ResponseWorkSummaryDTO;
import com.sants.gestaodeponto.domain.worklog.WorkLog;
import com.sants.gestaodeponto.exceptions.ResourceNotFoundException;
import com.sants.gestaodeponto.services.WorkLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("worklogs")
public class WorkLogController {

    private final WorkLogService workLogService;

    public WorkLogController(WorkLogService workLogService) {
        this.workLogService = workLogService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWorkLogDTO> registerWorkLog(@RequestBody @Valid RequestWorkLogDTO request) {
        try {
            WorkLog workLog = workLogService.registerWorkLog(request.logType(), request.timestamp());
            ResponseWorkLogDTO response = new ResponseWorkLogDTO(
                    workLog.getId(),
                    workLog.getUser().getId(),
                    workLog.getLogType(),
                    workLog.getTimestamp()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Erro ao registrar ponto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ResponseWorkLogDTO>> getAllWorkLogsForAuthenticatedUser() {
        List<WorkLog> workLogs = workLogService.getAllWorkLogsForAuthenticatedUser();
        List<ResponseWorkLogDTO> response = workLogs.stream()
                .map(workLog -> new ResponseWorkLogDTO(
                        workLog.getId(),
                        workLog.getUser().getId(),
                        workLog.getLogType(),
                        workLog.getTimestamp()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<ResponseWorkSummaryDTO> getWorkSummary() {
        try {
            ResponseWorkSummaryDTO summary = workLogService.getWorkSummary();
            return ResponseEntity.ok(summary);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Erro ao buscar resumo de jornada: " + e.getMessage());
        }
    }
}
