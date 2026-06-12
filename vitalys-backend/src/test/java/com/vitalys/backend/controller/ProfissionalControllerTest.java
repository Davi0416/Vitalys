package com.vitalys.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.exception.GlobalExceptionHandler;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.service.ProfissionalService;
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
class ProfissionalControllerTest {

    @Mock ProfissionalService profissionalService;
    @InjectMocks ProfissionalController profissionalController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Date DATA_VALIDA = Date.from(Instant.now().minus(365 * 30L, ChronoUnit.DAYS));

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profissionalController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private ProfissionalResponseDTO responseDTO() {
        return new ProfissionalResponseDTO(1L, "Dr. Carlos", "carlos@email.com",
                "52998224725", "21912345678", DATA_VALIDA);
    }

    private ProfissionalRequestDTO requestDTO() {
        return new ProfissionalRequestDTO("Dr. Carlos", "carlos@email.com",
                "52998224725", "21912345678", DATA_VALIDA);
    }

    @Test
    void findAll_retornaListaOk() throws Exception {
        when(profissionalService.findAll()).thenReturn(List.of(responseDTO()));

        mockMvc.perform(get("/vitalys/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Dr. Carlos"));
    }

    @Test
    void registrar_comBodyValido_retornaCreated() throws Exception {
        when(profissionalService.registrar(any())).thenReturn(responseDTO());

        mockMvc.perform(post("/vitalys/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Dr. Carlos"));
    }

    @Test
    void deleteProfissional_comIdExistente_retornaNoContent() throws Exception {
        doNothing().when(profissionalService).deletar(1L);

        mockMvc.perform(delete("/vitalys/profissionais/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProfissional_comIdInexistente_retornaNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Profissional", 99L))
                .when(profissionalService).deletar(99L);

        mockMvc.perform(delete("/vitalys/profissionais/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateProfissional_comBodyValido_retornaOk() throws Exception {
        when(profissionalService.editar(eq(1L), any())).thenReturn(responseDTO());

        mockMvc.perform(put("/vitalys/profissionais/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dr. Carlos"));
    }
}
