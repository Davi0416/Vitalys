package com.vitalys.backend.controller;

import com.vitalys.backend.dto.AtendimentoRequestDTO;
import com.vitalys.backend.dto.AtendimentoResponseDTO;
import com.vitalys.backend.service.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping(path = "/atendimentos")
    public ResponseEntity<AtendimentoResponseDTO> create(@RequestBody @Valid AtendimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoService.registrar(dto));
    }

    @GetMapping(path = "/atendimentos")
    public ResponseEntity<List<AtendimentoResponseDTO>> findAll() {
        return ResponseEntity.ok(atendimentoService.findAll());
    }

    @DeleteMapping(path = "/atendimentos/{id}")
    public ResponseEntity<Void> deleteAtendimento(@PathVariable Long id) {
        atendimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/atendimentos/{id}")
    public ResponseEntity<AtendimentoResponseDTO> updateAtendimento(
            @PathVariable Long id,
            @RequestBody @Valid AtendimentoRequestDTO dto) {
        return ResponseEntity.ok(atendimentoService.editar(id, dto));
    }
}
