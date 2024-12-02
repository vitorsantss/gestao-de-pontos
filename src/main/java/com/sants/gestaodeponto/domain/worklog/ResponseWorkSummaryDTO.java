package com.sants.gestaodeponto.domain.worklog;

public record ResponseWorkSummaryDTO(String totalWorked, String remainingTime, String extraHours, boolean completed) {
}
