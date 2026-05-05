package com.vitalys.backend.dto;

import com.vitalys.backend.model.Usuarios;

public record UsuariosResponseDTO(
        Long id,
        String login,
        Long idCargo,
        Long idProfissional,
        Boolean ativo) {

    public UsuariosResponseDTO(Usuarios u) {
        this(u.getId(), u.getLogin(), u.getIdCargo(), u.getIdProfissional(), u.getAtivo());
    }
}
