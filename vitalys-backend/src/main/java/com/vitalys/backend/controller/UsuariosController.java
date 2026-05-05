package com.vitalys.backend.controller;

import com.vitalys.backend.dto.UsuariosRequestDTO;
import com.vitalys.backend.dto.UsuariosResponseDTO;
import com.vitalys.backend.model.Usuarios;
import com.vitalys.backend.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/vitalys")
public class UsuariosController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @PostMapping(path = "/usuarios")
    public ResponseEntity<UsuariosResponseDTO> create(@RequestBody UsuariosRequestDTO dto) {
        Usuarios u = new Usuarios();
        u.setLogin(dto.login());
        u.setSenha(dto.senha());
        u.setIdCargo(dto.idCargo());
        u.setIdProfissional(dto.idProfissional());
        u.setAtivo(dto.ativo());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuariosResponseDTO(usuariosRepository.save(u)));
    }

    @GetMapping(path = "/usuarios")
    public ResponseEntity<List<UsuariosResponseDTO>> findAll() {
        List<UsuariosResponseDTO> usuarios = StreamSupport.stream(usuariosRepository.findAll().spliterator(), false)
                .map(UsuariosResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping(path = "/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuariosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/usuarios/{id}")
    public ResponseEntity<UsuariosResponseDTO> update(@PathVariable Long id, @RequestBody UsuariosRequestDTO dto) {
        Usuarios existente = usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        existente.setLogin(dto.login());
        existente.setSenha(dto.senha());
        existente.setIdCargo(dto.idCargo());
        existente.setIdProfissional(dto.idProfissional());
        existente.setAtivo(dto.ativo());
        return ResponseEntity.ok(new UsuariosResponseDTO(usuariosRepository.save(existente)));
    }
}
