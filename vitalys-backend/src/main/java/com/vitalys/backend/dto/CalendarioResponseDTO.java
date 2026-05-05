package com.vitalys.backend.dto;

import com.vitalys.backend.model.Calendario;

import java.util.Date;

public record CalendarioResponseDTO(
        Long id,
        String nome,
        Date data,
        String tipo,
        Long idAtendimento) {

    public CalendarioResponseDTO(Calendario c) {
        this(c.getId(), c.getNome(), c.getData(), c.getTipo(), c.getIdAtendimento());
    }
}
