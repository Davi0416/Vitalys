package com.vitalys.backend.controller;

import com.vitalys.backend.model.Usuarios;
import com.vitalys.backend.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class UsuariosController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @PostMapping(path = "/usuarios")
    public Usuarios create(@RequestBody Usuarios usuario) {
        return usuariosRepository.save(usuario);
    }

    @GetMapping(path = "/usuarios")
    public @ResponseBody Iterable<Usuarios> findAll() {
        return usuariosRepository.findAll();
    }
}