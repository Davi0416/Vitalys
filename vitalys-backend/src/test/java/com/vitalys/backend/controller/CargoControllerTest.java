package com.vitalys.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalys.backend.dto.CargoRequestDTO;
import com.vitalys.backend.dto.CargoResponseDTO;
import com.vitalys.backend.exception.GlobalExceptionHandler;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.service.CargoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CargoControllerTest {

    @Mock CargoService cargoService;
    @InjectMocks CargoController cargoController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cargoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findAll_retornaListaOk() throws Exception {
        when(cargoService.findAll()).thenReturn(List.of(new CargoResponseDTO(1L, "Médico", "ADMIN")));

        mockMvc.perform(get("/vitalys/cargos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cargo").value("Médico"))
                .andExpect(jsonPath("$[0].nivelAcesso").value("ADMIN"));
    }

    @Test
    void create_comBodyValido_retornaCreated() throws Exception {
        CargoRequestDTO dto = new CargoRequestDTO("Médico", "ADMIN");
        when(cargoService.registrar(any())).thenReturn(new CargoResponseDTO(1L, "Médico", "ADMIN"));

        mockMvc.perform(post("/vitalys/cargos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cargo").value("Médico"));
    }

    @Test
    void delete_comIdExistente_retornaNoContent() throws Exception {
        doNothing().when(cargoService).deletar(1L);

        mockMvc.perform(delete("/vitalys/cargos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_comIdInexistente_retornaNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Cargo", 99L))
                .when(cargoService).deletar(99L);

        mockMvc.perform(delete("/vitalys/cargos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void update_comBodyValido_retornaOk() throws Exception {
        CargoRequestDTO dto = new CargoRequestDTO("Enfermeiro", "USER");
        when(cargoService.editar(eq(1L), any())).thenReturn(new CargoResponseDTO(1L, "Enfermeiro", "USER"));

        mockMvc.perform(put("/vitalys/cargos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cargo").value("Enfermeiro"));
    }
}
