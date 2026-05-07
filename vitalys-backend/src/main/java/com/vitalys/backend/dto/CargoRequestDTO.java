package com.vitalys.backend.dto;


import jakarta.validation.constraints.NotBlank;

public record CargoRequestDTO(
        @NotBlank String cargo,
        @NotBlank String nivelAcesso) {
}
