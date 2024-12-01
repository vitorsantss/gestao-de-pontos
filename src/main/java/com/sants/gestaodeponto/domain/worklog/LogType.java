package com.sants.gestaodeponto.domain.worklog;

public enum LogType {
    CHECK_IN("check_in"),
    CHECK_OUT("check_out"),
    LUNCH_START("lunch_start"),
    LUNCH_END("lunch_end"),
    BREAK_START("break_start"),
    BREAK_END("break_end");

    private String logType;

    LogType(String logType) {
        this.logType = logType;
    }
}
