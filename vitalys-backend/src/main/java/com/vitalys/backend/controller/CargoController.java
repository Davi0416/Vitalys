package com.vitalys.backend.controller;

import com.vitalys.backend.dto.CargoRequestDTO;
import com.vitalys.backend.dto.CargoResponseDTO;
import com.vitalys.backend.service.CargoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @PostMapping(path = "/cargos")
    public ResponseEntity<CargoResponseDTO> create(@RequestBody @Valid CargoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.registrar(dto));
    }

    @GetMapping(path = "/cargos")
    public ResponseEntity<List<CargoResponseDTO>> findAll() {
        return ResponseEntity.ok(cargoService.findAll());
    }

    @DeleteMapping(path = "/cargos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cargoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/cargos/{id}")
    public ResponseEntity<CargoResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CargoRequestDTO dto) {
        return ResponseEntity.ok(cargoService.editar(id, dto));
    }
}
