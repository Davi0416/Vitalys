package com.vitalys.backend.service;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import com.vitalys.backend.service.rules.UniqueFieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final UniqueFieldValidator uniqueFieldValidator;

    private void verificarDadosUnicos(ProfissionalRequestDTO dto, Long idAtual) {
        uniqueFieldValidator.validar("CPF", dto.cpf(), idAtual,
                () -> profissionalRepository.existsByCpf(dto.cpf()),
                profissionalRepository::existsByCpfAndIdNot);

        uniqueFieldValidator.validar("email", dto.email(), idAtual,
                () -> profissionalRepository.existsByEmail(dto.email()),
                profissionalRepository::existsByEmailAndIdNot);

        uniqueFieldValidator.validar("telefone", dto.telefone(), idAtual,
                () -> profissionalRepository.existsByTelefone(dto.telefone()),
                profissionalRepository::existsByTelefoneAndIdNot);
    }

    @Cacheable("profissionais")
    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> findAll() {
        return profissionalRepository.findAll().stream()
                .map(ProfissionalResponseDTO::new)
                .toList();
    }

    @CacheEvict(value = "profissionais", allEntries = true)
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

    @CacheEvict(value = "profissionais", allEntries = true)
    @Transactional
    public ProfissionalResponseDTO editar(Long id, ProfissionalRequestDTO dto) {
        Profissional p = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        verificarDadosUnicos(dto, id);
        p.atualizar(dto);
        return new ProfissionalResponseDTO(profissionalRepository.save(p));
    }

    @CacheEvict(value = "profissionais", allEntries = true)
    @Transactional
    public void deletar(Long id) {
        Profissional p = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
        profissionalRepository.delete(p);
    }
}
