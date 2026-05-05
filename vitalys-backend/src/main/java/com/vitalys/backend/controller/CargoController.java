package com.vitalys.backend.controller;

import com.vitalys.backend.model.Cargo;
import com.vitalys.backend.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class CargoController {

    @Autowired
    private CargoRepository cargoRepository;

    @PostMapping(path = "/cargos")
    public ResponseEntity<Cargo> create(@RequestBody Cargo cargo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoRepository.save(cargo));
    }

    @GetMapping(path = "/cargos")
    public ResponseEntity<Iterable<Cargo>> findAll() {
        return ResponseEntity.ok(cargoRepository.findAll());
    }

    @DeleteMapping(path = "/cargos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cargoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/cargos/{id}")
    public ResponseEntity<Cargo> update(@PathVariable Long id, @RequestBody Cargo cargo) {
        Cargo existente = cargoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));
        existente.setCargo(cargo.getCargo());
        existente.setNivelAcesso(cargo.getNivelAcesso());
        return ResponseEntity.ok(cargoRepository.save(existente));
    }
}
