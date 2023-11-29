package com.roundtable.roundtable.member.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterMemberRequest(
        @NotBlank @Email
        String email,
        @NotBlank
        String password
) {
}
