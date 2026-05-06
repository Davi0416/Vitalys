package com.vitalys.backend.dto;

import java.time.LocalDateTime;

public record AtendimentoResponseDTO(
        Long id,
        Long idProfissional,
        String nomePaciente,
        String nomeProfissional,
        LocalDateTime dataEHoraMarcadas) {
}
