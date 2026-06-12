package com.vitalys.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalys.backend.dto.PacienteRequestDTO;
import com.vitalys.backend.dto.PacienteResponseDTO;
import com.vitalys.backend.exception.GlobalExceptionHandler;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PacienteControllerTest {

    @Mock PacienteService pacienteService;
    @InjectMocks PacienteController pacienteController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Date DATA_VALIDA = Date.from(Instant.now().minus(365 * 25L, ChronoUnit.DAYS));

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pacienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private PacienteResponseDTO responseDTO() {
        return new PacienteResponseDTO(1L, "Maria Silva", "52998224725",
                "maria@email.com", DATA_VALIDA, "Rua A, 1", "21987654321");
    }

    private PacienteRequestDTO requestDTO() {
        return new PacienteRequestDTO("Maria Silva", "52998224725",
                "maria@email.com", DATA_VALIDA, "Rua A, 1", "21987654321");
    }

    @Test
    void getPacientes_retornaListaOk() throws Exception {
        when(pacienteService.findAll()).thenReturn(List.of(responseDTO()));

        mockMvc.perform(get("/vitalys/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Maria Silva"))
                .andExpect(jsonPath("$[0].cpf").value("52998224725"));
    }

    @Test
    void addPaciente_comBodyValido_retornaCreated() throws Exception {
        when(pacienteService.registrar(any())).thenReturn(responseDTO());

        mockMvc.perform(post("/vitalys/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria Silva"));
    }

    @Test
    void deletePaciente_comIdExistente_retornaNoContent() throws Exception {
        doNothing().when(pacienteService).deletar(1L);

        mockMvc.perform(delete("/vitalys/pacientes/1"))
                .andExpect(status().isNoContent());

        verify(pacienteService).deletar(1L);
    }

    @Test
    void deletePaciente_comIdInexistente_retornaNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Paciente", 99L))
                .when(pacienteService).deletar(99L);

        mockMvc.perform(delete("/vitalys/pacientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updatePaciente_comBodyValido_retornaOk() throws Exception {
        when(pacienteService.editar(eq(1L), any())).thenReturn(responseDTO());

        mockMvc.perform(put("/vitalys/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Silva"));
    }

    @Test
    void updatePaciente_comIdInexistente_retornaNotFound() throws Exception {
        when(pacienteService.editar(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Paciente", 99L));

        mockMvc.perform(put("/vitalys/pacientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO())))
                .andExpect(status().isNotFound());
    }
}
