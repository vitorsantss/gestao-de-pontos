package com.sants.gestaodeponto.domain.user;

public record RegisterDTO(String name, String email, String password, UserRole role, WorkSchedule work_schedule) {
}
