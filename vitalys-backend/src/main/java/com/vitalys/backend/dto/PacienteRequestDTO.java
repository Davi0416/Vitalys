package com.vitalys.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record PacienteRequestDTO(
        @NotBlank String nome,
        @NotNull @CPF String cpf,
        @Email String email,
        @NotNull Date dataNascimento,
        String endereco,
        String telefone) {
}
