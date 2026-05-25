package com.vitalys.backend.service;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.exception.ConflictException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    private void verificarDadosUnicos(ProfissionalRequestDTO dto, Long idAtual) {
        boolean cpfExiste = idAtual != null
                ? profissionalRepository.existsByCpfAndIdNot(dto.cpf(), idAtual)
                : profissionalRepository.existsByCpf(dto.cpf());
        if (cpfExiste) throw new ConflictException("CPF", dto.cpf());

        boolean emailExiste = idAtual != null
                ? profissionalRepository.existsByEmailAndIdNot(dto.email(), idAtual)
                : profissionalRepository.existsByEmail(dto.email());
        if (emailExiste) throw new ConflictException("email", dto.email());

        boolean telefoneExiste = idAtual != null
                ? profissionalRepository.existsByTelefoneAndIdNot(dto.telefone(), idAtual)
                : profissionalRepository.existsByTelefone(dto.telefone());
        if (telefoneExiste) throw new ConflictException("telefone", dto.telefone());
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> findAll() {
        return profissionalRepository.findAll().stream()
                .map(ProfissionalResponseDTO::new)
                .toList();
    }

    @Transactional
    public ProfissionalResponseDTO registrar(ProfissionalRequestDTO dto) {
        verificarDadosUnicos(dto, null);
        Profissional p = Profissional.builder()
                .nome(dto.nome())
                .email(dto.email())
                .cpf(dto.cpf())
                .telefone(dto.telefone())
                .dataNascimento(dto.dataNascimento())
                .build();
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    @Transactional
    public ProfissionalResponseDTO editar(Long id, ProfissionalRequestDTO dto) {
        Profissional p = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        verificarDadosUnicos(dto, id);
        p.atualizar(dto);
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    @Transactional
    public void deletar(Long id) {
        Profissional p = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        profissionalRepository.delete(p);
    }
}
