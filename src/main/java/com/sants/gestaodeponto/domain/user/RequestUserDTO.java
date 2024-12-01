package com.sants.gestaodeponto.domain.user;

public record RequestUserDTO(String name, String email, UserRole role, WorkSchedule work_schedule) {
}
