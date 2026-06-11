package com.vitalys.backend.service;

import com.vitalys.backend.dto.PacienteRequestDTO;
import com.vitalys.backend.dto.PacienteResponseDTO;
import com.vitalys.backend.exception.ConflictException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.service.rules.UniqueFieldValidator;
import com.vitalys.backend.service.rules.VerificarDataNascimento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UniqueFieldValidator uniqueFieldValidator;
    private final VerificarDataNascimento verificarDataNascimento;

    private void verificarDadosUnicos(PacienteRequestDTO dto, Long idAtual) {
        uniqueFieldValidator.validar("CPF", dto.cpf(), idAtual,
                () -> pacienteRepository.existsByCpf(dto.cpf()),
                pacienteRepository::existsByCpfAndIdNot);

        uniqueFieldValidator.validar("email", dto.email(), idAtual,
                () -> pacienteRepository.existsByEmail(dto.email()),
                pacienteRepository::existsByEmailAndIdNot);

        uniqueFieldValidator.validar("telefone", dto.telefone(), idAtual,
                () -> pacienteRepository.existsByTelefone(dto.telefone()),
                pacienteRepository::existsByTelefoneAndIdNot);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(PacienteResponseDTO::new)
                .toList();
    }

    @Transactional
    public PacienteResponseDTO registrar(PacienteRequestDTO dto) {
        verificarDadosUnicos(dto, null);
        verificarDataNascimento.verificarData(dto.dataNascimento());
        Paciente p = Paciente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .dataNascimento(dto.dataNascimento())
                .endereco(dto.endereco())
                .telefone(dto.telefone())
                .build();
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    @Transactional
    public PacienteResponseDTO editar(Long id, PacienteRequestDTO dto) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        verificarDadosUnicos(dto, id);
        p.atualizar(dto);
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    @Transactional
    public void deletar(Long id) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        pacienteRepository.delete(p);
    }
}
