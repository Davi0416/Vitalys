package com.vitalys.backend.controller;

import com.vitalys.backend.dto.RegistrarPacienteDTO;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteService pacienteService;

    @PostMapping(path = "/pacientes")
    public ResponseEntity<Paciente> addPaciente(@RequestBody @Valid RegistrarPacienteDTO paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.registrar(paciente));
    }

    @GetMapping(path = "/pacientes")
    public ResponseEntity<Iterable<Paciente>> getPacientes() {
        return ResponseEntity.ok(pacienteRepository.findAll());
    }

    @DeleteMapping(path = "/pacientes/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/pacientes/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Long id, @RequestBody @Valid RegistrarPacienteDTO paciente) {
        return ResponseEntity.ok(pacienteService.editar(id, paciente));
    }
}
