package com.vitalys.backend.service;

import com.vitalys.backend.dto.CargoRequestDTO;
import com.vitalys.backend.dto.CargoResponseDTO;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Cargo;
import com.vitalys.backend.repository.CargoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @Mock CargoRepository cargoRepository;
    @InjectMocks CargoService cargoService;

    @Test
    void findAll_retornaListaDeCargos() {
        Cargo cargo = Cargo.builder().id(1L).cargo("Médico").nivelAcesso("ADMIN").build();
        when(cargoRepository.findAll()).thenReturn(List.of(cargo));

        List<CargoResponseDTO> resultado = cargoService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).cargo()).isEqualTo("Médico");
    }

    @Test
    void registrar_comDadosValidos_retornaCargoResponseDTO() {
        CargoRequestDTO dto = new CargoRequestDTO("Enfermeiro", "USER");
        Cargo salvo = Cargo.builder().id(1L).cargo("Enfermeiro").nivelAcesso("USER").build();
        when(cargoRepository.save(any())).thenReturn(salvo);

        CargoResponseDTO resultado = cargoService.registrar(dto);

        assertThat(resultado.cargo()).isEqualTo("Enfermeiro");
        assertThat(resultado.nivelAcesso()).isEqualTo("USER");
        verify(cargoRepository).save(any(Cargo.class));
    }

    @Test
    void editar_comCargoExistente_retornaCargoAtualizado() {
        Cargo existente = Cargo.builder().id(1L).cargo("Técnico").nivelAcesso("USER").build();
        CargoRequestDTO dto = new CargoRequestDTO("Médico", "ADMIN");
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any())).thenReturn(existente);

        CargoResponseDTO resultado = cargoService.editar(1L, dto);

        assertThat(resultado).isNotNull();
        verify(cargoRepository).save(existente);
    }

    @Test
    void editar_comIdInexistente_lancaResourceNotFoundException() {
        when(cargoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> cargoService.editar(99L, new CargoRequestDTO("X", "Y")));
    }

    @Test
    void deletar_comIdExistente_deletaCargo() {
        Cargo cargo = Cargo.builder().id(1L).cargo("Médico").nivelAcesso("ADMIN").build();
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));

        cargoService.deletar(1L);

        verify(cargoRepository).delete(cargo);
    }

    @Test
    void deletar_comIdInexistente_lancaResourceNotFoundException() {
        when(cargoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cargoService.deletar(99L));
    }
}
