package com.vitalys.backend.dto;

import java.util.Date;

public record CalendarioRequestDTO(
        String nome,
        Date data,
        String tipo,
        Long idAtendimento) {
}
