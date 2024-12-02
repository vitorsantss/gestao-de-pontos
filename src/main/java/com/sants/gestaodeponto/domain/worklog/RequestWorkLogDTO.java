package com.sants.gestaodeponto.domain.worklog;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestWorkLogDTO(@NotNull LogType logType, @NotNull LocalDateTime timestamp) {
}
