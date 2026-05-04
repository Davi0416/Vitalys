package com.vitalys.backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record ProfissionalRequestDTO(
        @NotBlank String nome,
        String email,
        @NotBlank String cpf,
        String telefone,
        Date dataNascimento) {
}
