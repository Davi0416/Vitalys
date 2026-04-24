package com.vitalys.backend.controller;

import com.vitalys.backend.model.Atendimento;
import com.vitalys.backend.repository.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "vitalys")
public class AtendimentoController {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @PostMapping(path = "/atendimentos")
    public Atendimento save(@RequestBody Atendimento atendimento) {
        return atendimentoRepository.save(atendimento);
    }

    @GetMapping(path = "/atendimentos")
    public @ResponseBody List<Atendimento> findAll() {
        return atendimentoRepository.findAll();
    }
}

