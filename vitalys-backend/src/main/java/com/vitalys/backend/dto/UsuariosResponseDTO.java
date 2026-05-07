package com.vitalys.backend.dto;

import com.vitalys.backend.model.Cargo;
import com.vitalys.backend.model.Usuarios;

public record UsuariosResponseDTO(
        Long id,
        String login,
        Cargo idCargo,
        Long idProfissional,
        Boolean ativo) {

    public UsuariosResponseDTO(Usuarios u) {
        this(u.getId(), u.getLogin(), u.getCargo(), u.getIdProfissional(), u.getAtivo());
    }
}
