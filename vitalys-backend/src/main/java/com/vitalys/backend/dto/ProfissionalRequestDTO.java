package com.vitalys.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record ProfissionalRequestDTO(
       @NotBlank @Size(min = 3, max = 50) String nome,
       @NotBlank @Email @Size(min = 3, max = 60) String email,
       @NotBlank @CPF String cpf,
       @NotBlank String telefone,
       @NotNull Date dataNascimento) {
}