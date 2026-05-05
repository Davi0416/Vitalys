package com.vitalys.backend.controller;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import com.vitalys.backend.dto.CalendarioResponseDTO;
import com.vitalys.backend.model.Calendario;
import com.vitalys.backend.repository.CalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/vitalys")
public class CalendarioController {

    @Autowired
    private CalendarioRepository calendarioRepository;

    @PostMapping(path = "/calendario")
    public ResponseEntity<CalendarioResponseDTO> addCalendario(@RequestBody CalendarioRequestDTO dto) {
        Calendario c = new Calendario();
        c.setNome(dto.nome());
        c.setData(dto.data());
        c.setTipo(dto.tipo());
        c.setIdAtendimento(dto.idAtendimento());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CalendarioResponseDTO(calendarioRepository.save(c)));
    }

    @GetMapping(path = "/calendario")
    public ResponseEntity<List<CalendarioResponseDTO>> findAllCalendario() {
        List<CalendarioResponseDTO> lista = StreamSupport.stream(calendarioRepository.findAll().spliterator(), false)
                .map(CalendarioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping(path = "/calendario/{id}")
    public ResponseEntity<Void> deleteCalendario(@PathVariable Long id) {
        calendarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/calendario/{id}")
    public ResponseEntity<CalendarioResponseDTO> updateCalendario(@PathVariable Long id, @RequestBody CalendarioRequestDTO dto) {
        Calendario existente = calendarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendário não encontrado"));
        existente.setNome(dto.nome());
        existente.setData(dto.data());
        existente.setTipo(dto.tipo());
        existente.setIdAtendimento(dto.idAtendimento());
        return ResponseEntity.ok(new CalendarioResponseDTO(calendarioRepository.save(existente)));
    }
}
