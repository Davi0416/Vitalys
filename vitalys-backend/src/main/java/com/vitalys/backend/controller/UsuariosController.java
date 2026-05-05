package com.vitalys.backend.controller;

import com.vitalys.backend.model.Usuarios;
import com.vitalys.backend.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class UsuariosController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @PostMapping(path = "/usuarios")
    public ResponseEntity<Usuarios> create(@RequestBody Usuarios usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosRepository.save(usuario));
    }

    @GetMapping(path = "/usuarios")
    public ResponseEntity<Iterable<Usuarios>> findAll() {
        return ResponseEntity.ok(usuariosRepository.findAll());
    }

    @DeleteMapping(path = "/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuariosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/usuarios/{id}")
    public ResponseEntity<Usuarios> update(@PathVariable Long id, @RequestBody Usuarios usuario) {
        Usuarios existente = usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        existente.setLogin(usuario.getLogin());
        existente.setSenha(usuario.getSenha());
        existente.setIdCargo(usuario.getIdCargo());
        existente.setIdProfissional(usuario.getIdProfissional());
        existente.setAtivo(usuario.getAtivo());
        return ResponseEntity.ok(usuariosRepository.save(existente));
    }
}
