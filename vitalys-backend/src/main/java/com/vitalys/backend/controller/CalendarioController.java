package com.vitalys.backend.controller;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import com.vitalys.backend.dto.CalendarioResponseDTO;
import com.vitalys.backend.repository.CalendarioRepository;
import com.vitalys.backend.service.CalendarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class CalendarioController {

    @Autowired
    private CalendarioRepository calendarioRepository;

    @Autowired
    private CalendarioService calendarioService;

    @PostMapping(path = "/calendario")
    public ResponseEntity<CalendarioResponseDTO> addCalendario(@RequestBody CalendarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarioService.registrar(dto));
    }

    @GetMapping(path = "/calendario")
    public ResponseEntity<List<CalendarioResponseDTO>> findAllCalendario() {
        return ResponseEntity.ok(calendarioService.findAll());
    }

    @DeleteMapping(path = "/calendario/{id}")
    public ResponseEntity<Void> deleteCalendario(@PathVariable Long id) {
        calendarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/calendario/{id}")
    public ResponseEntity<CalendarioResponseDTO> updateCalendario(@PathVariable Long id, @RequestBody CalendarioRequestDTO dto) {
        return ResponseEntity.ok(calendarioService.editar(id, dto));
    }
}
