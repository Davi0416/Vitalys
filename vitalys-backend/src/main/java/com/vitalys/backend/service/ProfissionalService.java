package com.vitalys.backend.service;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public ProfissionalResponseDTO registrar(ProfissionalRequestDTO dto) {
        Profissional p = new Profissional();
        p.setNome(dto.nome());
        p.setEmail(dto.email());
        p.setCpf(dto.cpf());
        p.setTelefone(dto.telefone());
        p.setDataNascimento(dto.dataNascimento());
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    public ProfissionalResponseDTO editar(Long id, ProfissionalRequestDTO dto) {
        Profissional profissionalExistente = profissionalRepository
                .findById(id)
                .orElse(null);

        profissionalExistente.setNome(dto.nome());
        profissionalExistente.setEmail(dto.email());
        profissionalExistente.setCpf(dto.cpf());
        profissionalExistente.setTelefone(dto.telefone());
        profissionalExistente.setDataNascimento(dto.dataNascimento());

        return new ProfissionalResponseDTO(profissionalRepository.save(profissionalExistente));
    }

    public void deletar(Long id){
        profissionalRepository.deleteById(id);
    }
}
