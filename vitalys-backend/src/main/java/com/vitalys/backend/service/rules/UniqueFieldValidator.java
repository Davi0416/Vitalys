package com.vitalys.backend.service.rules;

import com.vitalys.backend.dto.PacienteRequestDTO;
import com.vitalys.backend.exception.ConflictException;

public class VerificarDadosUnicos {
    private void verificarDadosUnicos(PacienteRequestDTO dto, Long idAtual) {

        boolean cpfExiste = idAtual != null
                ? pacienteRepository.existsByCpfAndIdNot(dto.cpf(), idAtual)
                : pacienteRepository.existsByCpf(dto.cpf());
        if (cpfExiste) throw new ConflictException("CPF", dto.cpf());

        boolean emailExiste = idAtual != null
                ? pacienteRepository.existsByEmailAndIdNot(dto.email(), idAtual)
                : pacienteRepository.existsByEmail(dto.email());
        if (emailExiste) throw new ConflictException("email", dto.email());

        boolean telefoneExiste = idAtual != null
                ? pacienteRepository.existsByTelefoneAndIdNot(dto.telefone(), idAtual)
                : pacienteRepository.existsByTelefone(dto.telefone());
        if (telefoneExiste) throw new ConflictException("telefone", dto.telefone());
    }
}
