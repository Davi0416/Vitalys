package com.vitalys.backend.dto;

import com.vitalys.backend.model.Cargo;

public record CargoResponseDTO(
        Long id,
        String cargo,
        String nivelAcesso) {

    public CargoResponseDTO(Cargo c) {
        this(c.getId(), c.getCargo(), c.getNivelAcesso());
    }
}
