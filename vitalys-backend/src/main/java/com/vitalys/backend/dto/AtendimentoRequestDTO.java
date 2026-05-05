package com.vitalys.backend.dto;

import java.time.LocalDateTime;

public record AtendimentoRequestDTO(
        Long idPaciente,
        Long idProfissional,
        LocalDateTime dataEHoraMarcadas) {
}
