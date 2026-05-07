package com.vitalys.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record ProfissionalRequestDTO(
       @NotBlank String nome,
       @NotBlank @Email String email,
       @NotBlank @CPF String cpf,
       @NotBlank String telefone,
       @NotNull Date dataNascimento) {
}