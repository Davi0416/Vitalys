package com.vitalys.backend.service;

import com.vitalys.backend.dto.PacienteResponseDTO;
import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.dto.UsuariosResponseDTO;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public List<ProfissionalResponseDTO> findAll() {
        return profissionalRepository.findAll().stream()
                .map(ProfissionalResponseDTO::new)
                .toList();
    }

    public ProfissionalResponseDTO registrar(ProfissionalRequestDTO dto) {
        Profissional p = new Profissional();
        p.atualizarDados(dto);
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    public ProfissionalResponseDTO editar(Long id, ProfissionalRequestDTO dto) {
        Profissional p = profissionalRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        p.atualizarDados(dto);
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    public void deletar(Long id){
        Profissional profissional = profissionalRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        profissionalRepository.delete(profissional);
    }
}
