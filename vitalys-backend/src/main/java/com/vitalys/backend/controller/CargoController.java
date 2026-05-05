package com.vitalys.backend.controller;

import com.vitalys.backend.dto.CargoRequestDTO;
import com.vitalys.backend.dto.CargoResponseDTO;
import com.vitalys.backend.model.Cargo;
import com.vitalys.backend.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/vitalys")
public class CargoController {

    @Autowired
    private CargoRepository cargoRepository;

    @PostMapping(path = "/cargos")
    public ResponseEntity<CargoResponseDTO> create(@RequestBody CargoRequestDTO dto) {
        Cargo cargo = new Cargo();
        cargo.setCargo(dto.cargo());
        cargo.setNivelAcesso(dto.nivelAcesso());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CargoResponseDTO(cargoRepository.save(cargo)));
    }

    @GetMapping(path = "/cargos")
    public ResponseEntity<List<CargoResponseDTO>> findAll() {
        List<CargoResponseDTO> cargos = StreamSupport.stream(cargoRepository.findAll().spliterator(), false)
                .map(CargoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cargos);
    }

    @DeleteMapping(path = "/cargos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cargoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/cargos/{id}")
    public ResponseEntity<CargoResponseDTO> update(@PathVariable Long id, @RequestBody CargoRequestDTO dto) {
        Cargo existente = cargoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));
        existente.setCargo(dto.cargo());
        existente.setNivelAcesso(dto.nivelAcesso());
        return ResponseEntity.ok(new CargoResponseDTO(cargoRepository.save(existente)));
    }
}
