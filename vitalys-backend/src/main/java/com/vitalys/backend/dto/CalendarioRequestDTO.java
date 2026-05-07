package com.vitalys.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CalendarioRequestDTO(
        @NotBlank String nome,
        @NotNull Date data,
        @NotBlank String tipo,
        @NotNull Long idAtendimento) {
}
