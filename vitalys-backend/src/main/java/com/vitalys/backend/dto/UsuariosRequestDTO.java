package com.vitalys.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuariosRequestDTO(
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull Long idCargo,
        @NotNull Long idProfissional,
        @NotNull Boolean ativo) {
}
