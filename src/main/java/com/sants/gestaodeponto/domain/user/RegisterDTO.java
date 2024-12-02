package com.sants.gestaodeponto.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(@NotBlank @NotNull String name,
                          @NotBlank @NotNull String email,
                          @NotBlank @NotNull String password,
                          @NotNull UserRole role,
                          @NotNull WorkSchedule work_schedule) {
}
