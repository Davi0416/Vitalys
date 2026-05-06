package com.vitalys.backend.service;

import com.vitalys.backend.dto.PacienteRequestDTO;
import com.vitalys.backend.dto.PacienteResponseDTO;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<PacienteResponseDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(PacienteResponseDTO::new)
                .toList();
    }

    public PacienteResponseDTO registrar(PacienteRequestDTO dto) {
        Paciente p = new Paciente();
        p.atualizarDados(dto);
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    public PacienteResponseDTO editar(Long id, PacienteRequestDTO dto) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        p.atualizarDados(dto);
        return new PacienteResponseDTO(pacienteRepository.save(p));
    }

    public void deletar(Long id){
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        pacienteRepository.delete(p);
    }
}
