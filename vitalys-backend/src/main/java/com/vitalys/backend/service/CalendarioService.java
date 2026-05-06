package com.vitalys.backend.service;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import com.vitalys.backend.dto.CalendarioResponseDTO;
import com.vitalys.backend.model.Calendario;
import com.vitalys.backend.repository.CalendarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarioService {

    private final CalendarioRepository calendarioRepository;

    public CalendarioService(CalendarioRepository calendarioRepository) {
        this.calendarioRepository = calendarioRepository;
    }

    public List<CalendarioResponseDTO> findAll() {
        return calendarioRepository.findAll().stream()
                .map(CalendarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CalendarioResponseDTO registrar(CalendarioRequestDTO dto) {
        Calendario c = new Calendario();
        c.setNome(dto.nome());
        c.setData(dto.data());
        c.setTipo(dto.tipo());
        c.setIdAtendimento(dto.idAtendimento());
        return new CalendarioResponseDTO(calendarioRepository.save(c));
    }

    public CalendarioResponseDTO editar(Long id, CalendarioRequestDTO dto) {
        Calendario existente = calendarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendário não encontrado"));
        existente.setNome(dto.nome());
        existente.setData(dto.data());
        existente.setTipo(dto.tipo());
        existente.setIdAtendimento(dto.idAtendimento());
        return new CalendarioResponseDTO(calendarioRepository.save(existente));
    }
}
