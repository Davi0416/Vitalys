package com.vitalys.backend.controller;


import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @PostMapping(path="/profissionais")
    public Profissional create(@RequestBody Profissional profissional){
        return profissionalRepository.save(profissional);
    }

    @GetMapping(path = "/profissionais")
    public @ResponseBody Iterable<Profissional> findAll(){
        return profissionalRepository.findAll();
    }
}
