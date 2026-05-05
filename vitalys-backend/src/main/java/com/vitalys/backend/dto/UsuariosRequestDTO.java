package com.vitalys.backend.dto;

public record UsuariosRequestDTO(
        String login,
        String senha,
        Long idCargo,
        Long idProfissional,
        Boolean ativo) {
}
