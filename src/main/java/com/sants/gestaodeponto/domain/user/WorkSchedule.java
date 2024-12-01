package com.sants.gestaodeponto.domain.user;

public enum WorkSchedule {

    EIGHT_HOURS("8_hours"),
    SIX_HOURS("6_hours");

    private String work_schedule;

    WorkSchedule(String work_schedule) {
        this.work_schedule = work_schedule;
    }
}
