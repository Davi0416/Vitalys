package com.vitalys.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record CalendarioRequestDTO(
        @NotBlank @Size(min = 3, max = 50) String nome,
        @NotNull Date data,
        @NotBlank @Size(min = 3, max = 50) String tipo,
        @NotNull Long idAtendimento) {
}
