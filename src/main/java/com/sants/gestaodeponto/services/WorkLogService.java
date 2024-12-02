package com.sants.gestaodeponto.services;

import com.sants.gestaodeponto.domain.user.User;
import com.sants.gestaodeponto.domain.user.WorkSchedule;
import com.sants.gestaodeponto.domain.worklog.LogType;
import com.sants.gestaodeponto.domain.worklog.ResponseWorkSummaryDTO;
import com.sants.gestaodeponto.domain.worklog.WorkLog;
import com.sants.gestaodeponto.exceptions.ValidationException;
import com.sants.gestaodeponto.repositories.WorkLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkLogService {

    @Autowired
    private WorkLogRepository workLogRepository;

    public WorkLog registerWorkLog(LogType logType, LocalDateTime timestamp) {
        User user = getAuthenticatedUser();
        validateLogSequence(user.getId(), logType);

        WorkLog workLog = new WorkLog(user, logType, timestamp);
        return workLogRepository.save(workLog);
    }

    public List<WorkLog> getAllWorkLogsForAuthenticatedUser() {
        User user = getAuthenticatedUser();
        return workLogRepository.findByUserId(user.getId());
    }

    public ResponseWorkSummaryDTO getWorkSummary() {
        User user = getAuthenticatedUser();
        List<WorkLog> workLogs = getWorkLogsForToday();

        Duration totalWorked = calculateTotalWorked(workLogs);
        Duration requiredDuration = getRequiredWorkDuration(user.getWork_schedule());

        boolean completed = totalWorked.compareTo(requiredDuration) >= 0;
        Duration remaining = completed ? Duration.ZERO : requiredDuration.minus(totalWorked);
        Duration extra = completed ? totalWorked.minus(requiredDuration) : Duration.ZERO;

        return new ResponseWorkSummaryDTO(
                formatDuration(totalWorked),
                formatDuration(remaining),
                formatDuration(extra),
                completed
        );
    }

    private List<WorkLog> getWorkLogsForToday() {
        User user = getAuthenticatedUser();
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        return workLogRepository.findByUserIdAndTimestampAfter(user.getId(), startOfDay);
    }

    private void validateLogSequence(String userId, LogType logType) {
        User user = getAuthenticatedUser();

        if (user.getWork_schedule() == WorkSchedule.SIX_HOURS) {
            if (logType == LogType.LUNCH_START || logType == LogType.LUNCH_END
                    || logType == LogType.BREAK_START || logType == LogType.BREAK_END) {
                throw new ValidationException("Usuários com jornada de 6 horas não podem registrar pausas.");
            }
        }

        List<WorkLog> logs = workLogRepository.findByUserIdOrderByTimestampDesc(userId);

        if (requiresMatchingStart(logType)) {
            if (!hasUnmatchedStart(logs, getMatchingStartType(logType))) {
                throw new ValidationException("Não é possível registrar " + logType + " sem um " + getMatchingStartType(logType) + " correspondente.");
            }
        }

        if (!logs.isEmpty()) {
            LogType lastLogType = logs.get(0).getLogType();

            if (lastLogType == logType) {
                throw new ValidationException("Registro duplicado para o mesmo tipo consecutivo.");
            }
        }
    }

    private boolean requiresMatchingStart(LogType logType) {
        return logType == LogType.CHECK_OUT || logType == LogType.LUNCH_END || logType == LogType.BREAK_END;
    }

    private LogType getMatchingStartType(LogType logType) {
        return switch (logType) {
            case CHECK_OUT -> LogType.CHECK_IN;
            case LUNCH_END -> LogType.LUNCH_START;
            case BREAK_END -> LogType.BREAK_START;
            default -> null;
        };
    }

    private boolean hasUnmatchedStart(List<WorkLog> logs, LogType startType) {
        int unmatchedStartCount = 0;

        for (WorkLog log : logs) {
            if (log.getLogType() == startType) {
                unmatchedStartCount++;
            } else if (log.getLogType() == getMatchingEndType(startType)) {
                unmatchedStartCount--;
            }

            if (unmatchedStartCount < 0) {
                break;
            }
        }

        return unmatchedStartCount > 0;
    }

    private LogType getMatchingEndType(LogType startType) {
        return switch (startType) {
            case CHECK_IN -> LogType.CHECK_OUT;
            case LUNCH_START -> LogType.LUNCH_END;
            case BREAK_START -> LogType.BREAK_END;
            default -> null;
        };
    }

    private Duration calculateTotalWorked(List<WorkLog> workLogs) {
        Duration total = Duration.ZERO;
        for (int i = 0; i < workLogs.size() - 1; i += 2) {
            WorkLog startLog = workLogs.get(i);
            WorkLog endLog = workLogs.get(i + 1);

            if (startLog.getLogType() == LogType.CHECK_IN || startLog.getLogType() == LogType.LUNCH_END || startLog.getLogType() == LogType.BREAK_END) {
                total = total.plus(Duration.between(startLog.getTimestamp(), endLog.getTimestamp()));
            }
        }
        return total;
    }

    private Duration getRequiredWorkDuration(WorkSchedule schedule) {
        return schedule == WorkSchedule.SIX_HOURS ? Duration.ofHours(6) : Duration.ofHours(8);
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%02d:%02d", hours, minutes);
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
