package com.vitalys.backend.service;

import com.vitalys.backend.dto.RegistrarPacienteDTO;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente registrar(RegistrarPacienteDTO dto) {
        return pacienteRepository.save(new Paciente().registrar(dto));
    }

    public Paciente editar(Long id, RegistrarPacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        paciente.atualizar(dto);
        return pacienteRepository.save(paciente);
    }
}
