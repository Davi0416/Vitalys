package com.vitalys.backend.dto;

import com.vitalys.backend.model.Cargo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuariosRequestDTO(
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull Cargo cargo,
        @NotNull Long idProfissional,
        @NotNull Boolean ativo) {
}
