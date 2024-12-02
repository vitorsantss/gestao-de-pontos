package com.sants.gestaodeponto.domain.worklog;

import java.time.LocalDateTime;

public record ResponseWorkLogDTO(String id, String userId, LogType logType, LocalDateTime timestamp) {
}
