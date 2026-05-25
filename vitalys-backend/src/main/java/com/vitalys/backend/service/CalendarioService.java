package com.vitalys.backend.service;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import com.vitalys.backend.dto.CalendarioResponseDTO;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Calendario;
import com.vitalys.backend.repository.CalendarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CalendarioService {

    private final CalendarioRepository calendarioRepository;

    public CalendarioService(CalendarioRepository calendarioRepository) {
        this.calendarioRepository = calendarioRepository;
    }

    @Transactional(readOnly = true)
    public List<CalendarioResponseDTO> findAll() {
        return calendarioRepository.findAll().stream()
                .map(CalendarioResponseDTO::new)
                .toList();
    }

    @Transactional
    public CalendarioResponseDTO registrar(CalendarioRequestDTO dto) {
        Calendario c = Calendario.builder()
                .nome(dto.nome())
                .data(dto.data())
                .tipo(dto.tipo())
                .idAtendimento(dto.idAtendimento())
                .build();
        return new CalendarioResponseDTO(calendarioRepository.save(c));
    }

    @Transactional
    public CalendarioResponseDTO editar(Long id, CalendarioRequestDTO dto) {
        Calendario c = calendarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data", id));
        c.atualizar(dto);
        return new CalendarioResponseDTO(calendarioRepository.save(c));
    }

    @Transactional
    public void deletar(Long id) {
        Calendario c = calendarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data", id));
        calendarioRepository.delete(c);
    }
}
