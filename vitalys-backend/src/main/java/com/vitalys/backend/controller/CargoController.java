package com.vitalys.backend.controller;

import com.vitalys.backend.dto.CargoRequestDTO;
import com.vitalys.backend.dto.CargoResponseDTO;
import com.vitalys.backend.repository.CargoRepository;
import com.vitalys.backend.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class CargoController {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private CargoService cargoService;

    @PostMapping(path = "/cargos")
    public ResponseEntity<CargoResponseDTO> create(@RequestBody CargoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.registrar(dto));
    }

    @GetMapping(path = "/cargos")
    public ResponseEntity<List<CargoResponseDTO>> findAll() {
        return ResponseEntity.ok(cargoService.findAll());
    }

    @DeleteMapping(path = "/cargos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if(id == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        cargoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/cargos/{id}")
    public ResponseEntity<CargoResponseDTO> update(@PathVariable Long id, @RequestBody CargoRequestDTO dto) {
        if(id == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cargoService.editar(id, dto));
    }
}
