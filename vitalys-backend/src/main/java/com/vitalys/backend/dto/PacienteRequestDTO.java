package com.vitalys.backend.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record PacienteRequestDTO(
        @NotBlank String nome,
        @CPF String cpf,
        String email,
        Date dataNascimento,
        String endereco,
        String telefone) {
}
