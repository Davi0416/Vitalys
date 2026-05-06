package com.vitalys.backend.service;

import com.vitalys.backend.dto.CargoRequestDTO;
import com.vitalys.backend.dto.CargoResponseDTO;
import com.vitalys.backend.model.Cargo;
import com.vitalys.backend.repository.CargoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;

    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    public List<CargoResponseDTO> findAll() {
        return cargoRepository.findAll().stream()
                .map(CargoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CargoResponseDTO registrar(CargoRequestDTO dto) {
        Cargo cargo = new Cargo();
        cargo.setCargo(dto.cargo());
        cargo.setNivelAcesso(dto.nivelAcesso());
        return new CargoResponseDTO(cargoRepository.save(cargo));
    }

    public CargoResponseDTO editar(Long id, CargoRequestDTO dto) {
        Cargo existente = cargoRepository.findById(id)
                .orElse(null);
        assert existente != null;
        existente.setCargo(dto.cargo());
        existente.setNivelAcesso(dto.nivelAcesso());
        return new CargoResponseDTO(cargoRepository.save(existente));
    }

    public void deletar(Long id){
        cargoRepository.deleteById(id);
    }
}
