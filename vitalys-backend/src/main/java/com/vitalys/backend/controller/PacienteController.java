package com.vitalys.backend.controller;

import com.vitalys.backend.dto.PacienteRequestDTO;
import com.vitalys.backend.dto.PacienteResponseDTO;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteService pacienteService;

    @PostMapping(path = "/pacientes")
    public ResponseEntity<PacienteResponseDTO> addPaciente(@RequestBody @Valid PacienteRequestDTO paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.registrar(paciente));
    }

    @GetMapping(path = "/pacientes")
    public ResponseEntity<List<PacienteResponseDTO>> getPacientes() {
        return ResponseEntity.ok(pacienteService.findAll());
    }

    @DeleteMapping(path = "/pacientes/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/pacientes/{id}")
    public ResponseEntity<PacienteResponseDTO> updatePaciente(@PathVariable Long id, @RequestBody @Valid PacienteRequestDTO paciente) {
        return ResponseEntity.ok(pacienteService.editar(id, paciente));
    }
}
