package com.vitalys.backend.controller;


import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @PostMapping(path="/profissionais")
    public ResponseEntity<ProfissionalResponseDTO> registrar(@RequestBody @Valid ProfissionalRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.registrar(dto));
    }

    @GetMapping(path = "/profissionais")
    public ResponseEntity<List<ProfissionalResponseDTO>> findAll(){
        return ResponseEntity.ok(profissionalService.findAll());
    }

    @DeleteMapping(path = "/profissionais/{id}")
    public ResponseEntity<Void> deleteProfissional(@PathVariable Long id) {
        profissionalService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profissionais/{id}")
    public ResponseEntity<ProfissionalResponseDTO> updateProfissional(
            @PathVariable Long id,
            @RequestBody @Valid ProfissionalRequestDTO profissional) {
        return ResponseEntity.ok(profissionalService.editar(id, profissional));
    }
}
