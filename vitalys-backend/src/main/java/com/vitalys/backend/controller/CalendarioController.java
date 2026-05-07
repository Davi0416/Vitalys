package com.vitalys.backend.controller;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import com.vitalys.backend.dto.CalendarioResponseDTO;
import com.vitalys.backend.service.CalendarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class CalendarioController {

    private final CalendarioService calendarioService;

    public CalendarioController(CalendarioService calendarioService) {
        this.calendarioService = calendarioService;
    }

    @PostMapping(path = "/calendario")
    public ResponseEntity<CalendarioResponseDTO> addCalendario(@RequestBody @Valid CalendarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarioService.registrar(dto));
    }

    @GetMapping(path = "/calendario")
    public ResponseEntity<List<CalendarioResponseDTO>> findAllCalendario() {
        return ResponseEntity.ok(calendarioService.findAll());
    }

    @DeleteMapping(path = "/calendario/{id}")
    public ResponseEntity<Void> deleteCalendario(@PathVariable Long id) {
        calendarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/calendario/{id}")
    public ResponseEntity<CalendarioResponseDTO> updateCalendario(@PathVariable Long id, @RequestBody @Valid CalendarioRequestDTO dto) {
        return ResponseEntity.ok(calendarioService.editar(id, dto));
    }
}
