package com.vitalys.backend.controller;

import com.vitalys.backend.model.Cargo;
import com.vitalys.backend.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/vitalys")
public class CargoController {

    @Autowired
    private CargoRepository cargoRepository;

    @GetMapping(path = "/cargos")
    public @ResponseBody Iterable<Cargo> findAll() {
        return cargoRepository.findAll();
    }
}