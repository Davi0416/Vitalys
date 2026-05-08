package com.vitalys.backend.service;

import com.vitalys.backend.dto.PacienteRequestDTO;
import com.vitalys.backend.dto.PacienteResponseDTO;
import com.vitalys.backend.exception.ConflictException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

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

    public List<PacienteResponseDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(PacienteResponseDTO::new)
                .toList();
    }

    public PacienteResponseDTO registrar(PacienteRequestDTO dto) {
        verificarDadosUnicos(dto, null);
        Paciente p = new Paciente();
        p.atualizarDados(dto);
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    public PacienteResponseDTO editar(Long id, PacienteRequestDTO dto) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        verificarDadosUnicos(dto, id);
        p.atualizarDados(dto);
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    public void deletar(Long id) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        pacienteRepository.delete(p);
    }
}
