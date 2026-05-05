package com.vitalys.backend.dto;

import com.vitalys.backend.model.Profissional;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record ProfissionalResponseDTO(
        Long id,
        @NotBlank String nome,
        String email,
        @NotBlank String cpf,
        String telefone,
        Date dataNascimento)
{
    public ProfissionalResponseDTO (Profissional p){
        this(p.getId(), p.getNome(), p.getEmail(),  p.getCpf(), p.getTelefone(), p.getDataNascimento());
    }
}
