package com.roundtable.roundtable.business.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank @Email
        String email,
        @NotBlank
        String password
) {
}
