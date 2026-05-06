package com.vitalys.backend.service;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import com.vitalys.backend.dto.CalendarioResponseDTO;
import com.vitalys.backend.exception.ResourceNotFoundException;
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
                .toList();
    }

    public CalendarioResponseDTO registrar(CalendarioRequestDTO dto) {
        Calendario c = new Calendario();
        c.atualizarDados(dto);
        return new CalendarioResponseDTO(calendarioRepository.save(c));
    }

    public CalendarioResponseDTO editar(Long id, CalendarioRequestDTO dto) {
        Calendario c = calendarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data", id));
        c.atualizarDados(dto);
        return new CalendarioResponseDTO(calendarioRepository.save(c));
    }

    public void deletar(Long id){
        Calendario existente = calendarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data", id));
        calendarioRepository.delete(existente);
    }
}
