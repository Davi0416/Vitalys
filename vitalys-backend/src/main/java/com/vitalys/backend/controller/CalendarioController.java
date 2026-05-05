package com.vitalys.backend.controller;

import com.vitalys.backend.model.Calendario;
import com.vitalys.backend.repository.CalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class CalendarioController {

    @Autowired
    private CalendarioRepository calendarioRepository;

    @PostMapping(path = "/calendario")
    public ResponseEntity<Calendario> addCalendario(@RequestBody Calendario calendario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarioRepository.save(calendario));
    }

    @GetMapping(path = "/calendario")
    public ResponseEntity<Iterable<Calendario>> findAllCalendario() {
        return ResponseEntity.ok(calendarioRepository.findAll());
    }

    @DeleteMapping(path = "/calendario/{id}")
    public ResponseEntity<Void> deleteCalendario(@PathVariable Long id) {
        calendarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/calendario/{id}")
    public ResponseEntity<Calendario> updateCalendario(@PathVariable Long id, @RequestBody Calendario calendario) {
        Calendario existente = calendarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendário não encontrado"));
        existente.setNome(calendario.getNome());
        existente.setData(calendario.getData());
        existente.setTipo(calendario.getTipo());
        existente.setIdAtendimento(calendario.getIdAtendimento());
        return ResponseEntity.ok(calendarioRepository.save(existente));
    }
}
