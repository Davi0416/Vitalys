package com.vitalys.backend.controller;

import com.vitalys.backend.dto.UsuariosRequestDTO;
import com.vitalys.backend.dto.UsuariosResponseDTO;
import com.vitalys.backend.service.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PostMapping(path = "/usuarios")
    public ResponseEntity<UsuariosResponseDTO> create(@RequestBody UsuariosRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosService.registrar(dto));
    }

    @GetMapping(path = "/usuarios")
    public ResponseEntity<List<UsuariosResponseDTO>> findAll() {
        return ResponseEntity.ok(usuariosService.findAll());
    }

    @DeleteMapping(path = "/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuariosService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/usuarios/{id}")
    public ResponseEntity<UsuariosResponseDTO> update(@PathVariable Long id, @RequestBody UsuariosRequestDTO dto) {
        return ResponseEntity.ok(usuariosService.editar(id, dto));
    }
}
