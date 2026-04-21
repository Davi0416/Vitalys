package com.vitalys.backend.controller;

import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping(path="/vitalys")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping(path="/pacientes")
    public Paciente addPaciente(@RequestBody Paciente paciente){
        return pacienteRepository.save(paciente);
    }

    @GetMapping(path = "/pacientes")
    public @ResponseBody Iterable<Paciente> getPacientes(){
        return pacienteRepository.findAll();
    }
}
