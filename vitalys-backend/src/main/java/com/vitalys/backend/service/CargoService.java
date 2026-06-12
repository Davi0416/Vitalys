package com.vitalys.backend.service;

import com.vitalys.backend.dto.CargoRequestDTO;
import com.vitalys.backend.dto.CargoResponseDTO;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Cargo;
import com.vitalys.backend.repository.CargoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;

    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @Cacheable("cargos")
    @Transactional(readOnly = true)
    public List<CargoResponseDTO> findAll() {
        return cargoRepository.findAll().stream()
                .map(CargoResponseDTO::new)
                .toList();
    }

    @CacheEvict(value = "cargos", allEntries = true)
    @Transactional
    public CargoResponseDTO registrar(CargoRequestDTO dto) {
        Cargo cargo = Cargo.builder()
                .cargo(dto.cargo())
                .nivelAcesso(dto.nivelAcesso())
                .build();
        return new CargoResponseDTO(cargoRepository.save(cargo));
    }

    @CacheEvict(value = "cargos", allEntries = true)
    @Transactional
    public CargoResponseDTO editar(Long id, CargoRequestDTO dto) {
        Cargo existente = cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo", id));
        existente.atualizar(dto);
        return new CargoResponseDTO(cargoRepository.save(existente));
    }

    @CacheEvict(value = "cargos", allEntries = true)
    @Transactional
    public void deletar(Long id) {
        Cargo existente = cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo", id));
        cargoRepository.delete(existente);
    }
}
