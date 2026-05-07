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

    private void verificarDadosUnicos(PacienteRequestDTO dto){
        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new ConflictException("CPF", dto.cpf());
        }
        if (pacienteRepository.existsByEmail(dto.email())) {
            throw new ConflictException("email", dto.email());
        }
        if (pacienteRepository.existsByTelefone(dto.telefone())) {
            throw new ConflictException("telefone", dto.telefone());
        }
    }

    public List<PacienteResponseDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(PacienteResponseDTO::new)
                .toList();
    }

    public PacienteResponseDTO registrar(PacienteRequestDTO dto) {
        verificarDadosUnicos(dto);
        Paciente p = new Paciente();
        p.atualizarDados(dto);
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    public PacienteResponseDTO editar(Long id, PacienteRequestDTO dto) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        verificarDadosUnicos(dto);
        p.atualizarDados(dto);
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    public void deletar(Long id){
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        pacienteRepository.delete(p);
    }
}
