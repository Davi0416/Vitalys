package com.vitalys.backend.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record ProfissionalRequestDTO(
        @NotBlank String nome,
        String email,
        @NotBlank @CPF String cpf,
        String telefone,
        Date dataNascimento) {
}