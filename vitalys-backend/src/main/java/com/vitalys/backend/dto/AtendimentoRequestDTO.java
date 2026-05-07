package com.vitalys.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AtendimentoRequestDTO(
        @NotNull Long idPaciente,
        @NotNull Long idProfissional,
        @NotNull LocalDateTime dataEHoraMarcadas) {
}
