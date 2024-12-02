package com.sants.gestaodeponto.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(@NotBlank @NotNull String email, @NotBlank @NotNull String password) {
}
