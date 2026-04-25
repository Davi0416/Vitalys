package com.vitalys.backend.controller;


import com.vitalys.backend.dto.RegistrarProfissionalDTO;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @PostMapping(path="/profissionais")
    public Profissional create(@RequestBody RegistrarProfissionalDTO profissional){
        Profissional newProfissional = new Profissional().registrar(profissional);
        return profissionalRepository.save(newProfissional);
    }

    @GetMapping(path = "/profissionais")
    public @ResponseBody Iterable<Profissional> findAll(){
        return profissionalRepository.findAll();
    }

    @DeleteMapping(path = "/profissinais/{id}")
    public void deleteProfissional(@PathVariable Long id) {
       profissionalRepository.deleteById(id);
    }

    @PutMapping(path = "/profissionais/{id}")
    public Profissional updateProfissional(@PathVariable Long id, @RequestBody @Valid RegistrarProfissionalDTO profissional) {
        Profissional profissionalExistente = profissionalRepository.findById(id).orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        profissionalExistente.atualizar(profissional);
        return profissionalRepository.save(profissionalExistente);
    }
}
