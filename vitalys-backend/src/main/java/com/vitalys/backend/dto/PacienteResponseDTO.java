package com.vitalys.backend.dto;

import com.vitalys.backend.model.Paciente;

import java.util.Date;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        Date dataNascimento,
        String endereco,
        String telefone) {

    public PacienteResponseDTO(Paciente p) {
        this(p.getId(), p.getNome(), p.getCpf(), p.getEmail(),
                p.getDataNascimento(), p.getEndereco(), p.getTelefone());
    }
}
