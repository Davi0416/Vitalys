package com.vitalys.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record PacienteRequestDTO(
        @NotBlank @Size(min = 3, max = 50) String nome,
        @NotNull @CPF String cpf,
        @Email @Size(min = 3, max = 60) String email,
        @NotNull Date dataNascimento,
        @Size(min = 5, max = 100) String endereco,
        @Size(min = 11, max = 11) String telefone) {
}
