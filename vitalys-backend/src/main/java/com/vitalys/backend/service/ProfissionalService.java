package com.vitalys.backend.service;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.exception.ConflictException;
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

    private void verificarDadosUnicos(ProfissionalRequestDTO dto) {
        if (profissionalRepository.existsByCpf(dto.cpf())) {
            throw new ConflictException("CPF",  dto.cpf());
        }
        if (profissionalRepository.existsByEmail(dto.email())) {
            throw new ConflictException("email",  dto.email());
        }
        if (profissionalRepository.existsByTelefone(dto.telefone())) {
            throw new ConflictException("telefone",  dto.telefone());
        }
    }

    public List<ProfissionalResponseDTO> findAll() {
        return profissionalRepository.findAll().stream()
                .map(ProfissionalResponseDTO::new)
                .toList();
    }

    public ProfissionalResponseDTO registrar(ProfissionalRequestDTO dto) {
        Profissional p = new Profissional();
        verificarDadosUnicos(dto);
        p.atualizarDados(dto);
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    public ProfissionalResponseDTO editar(Long id, ProfissionalRequestDTO dto) {
        Profissional p = profissionalRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        verificarDadosUnicos(dto);
        p.atualizarDados(dto);
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    public void deletar(Long id){
        Profissional profissional = profissionalRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        profissionalRepository.delete(profissional);
    }
}
