package com.vitalys.backend.controller;

import com.vitalys.backend.dto.RegistrarPacienteDTO;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping(path="/vitalys")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping(path = "/pacientes")
    public Paciente addPaciente(@RequestBody RegistrarPacienteDTO paciente) {
        Paciente newPaciente = new Paciente().registrar(paciente);
        return pacienteRepository.save(newPaciente);
    }

    @GetMapping(path = "/pacientes")
    public @ResponseBody Iterable<Paciente> getPacientes() {
        return pacienteRepository.findAll();
    }


    @DeleteMapping(path = "/pacientes/{id}")
    public void deletePaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
    }

    @PutMapping(path = "/pacientes/{id}")
    public Paciente updatePaciente(@PathVariable Long id, @RequestBody @Valid RegistrarPacienteDTO paciente) {
        Paciente pacienteExistente = pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        pacienteExistente.atualizar(paciente);
        return pacienteRepository.save(pacienteExistente);
    }
}
