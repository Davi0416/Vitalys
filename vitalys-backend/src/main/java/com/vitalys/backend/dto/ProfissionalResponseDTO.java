package com.vitalys.backend.dto;

import com.vitalys.backend.model.Profissional;

import java.util.Date;

public record ProfissionalResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String telefone,
        Date dataNascimento)
{
    public ProfissionalResponseDTO (Profissional p){
        this(p.getId(), p.getNome(), p.getEmail(),  p.getCpf(), p.getTelefone(), p.getDataNascimento());
    }
}
