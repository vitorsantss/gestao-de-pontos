package com.sants.gestaodeponto.domain.user;

public record ResponseUserDTO(String id, String name, String email, UserRole role, WorkSchedule work_schedule) {
}
