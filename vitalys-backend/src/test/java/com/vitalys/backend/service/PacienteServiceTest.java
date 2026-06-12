package com.vitalys.backend.service;

import com.vitalys.backend.dto.PacienteRequestDTO;
import com.vitalys.backend.dto.PacienteResponseDTO;
import com.vitalys.backend.exception.ConflictException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.service.rules.UniqueFieldValidator;
import com.vitalys.backend.service.rules.VerificarDataNascimento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock PacienteRepository pacienteRepository;
    @Mock UniqueFieldValidator uniqueFieldValidator;
    @Mock VerificarDataNascimento verificarDataNascimento;
    @InjectMocks PacienteService pacienteService;

    private static final Date DATA_VALIDA = Date.from(Instant.now().minus(365 * 25L, ChronoUnit.DAYS));

    private PacienteRequestDTO dtoValido() {
        return new PacienteRequestDTO("Maria Silva", "52998224725",
                "maria@email.com", DATA_VALIDA, "Rua das Flores, 123", "21987654321");
    }

    private Paciente pacienteBuilder(Long id) {
        return Paciente.builder()
                .id(id).nome("Maria Silva").cpf("52998224725")
                .email("maria@email.com").dataNascimento(DATA_VALIDA)
                .endereco("Rua das Flores, 123").telefone("21987654321")
                .build();
    }

    @Test
    void findAll_retornaListaDePacientes() {
        when(pacienteRepository.findAll()).thenReturn(List.of(pacienteBuilder(1L)));

        List<PacienteResponseDTO> resultado = pacienteService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nome()).isEqualTo("Maria Silva");
    }

    @Test
    void registrar_comDadosValidos_retornaPacienteResponseDTO() {
        Paciente salvo = pacienteBuilder(1L);
        when(pacienteRepository.save(any())).thenReturn(salvo);

        PacienteResponseDTO resultado = pacienteService.registrar(dtoValido());

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Maria Silva");
        verify(pacienteRepository).save(any(Paciente.class));
        verify(verificarDataNascimento).verificarData(DATA_VALIDA);
    }

    @Test
    void registrar_comCpfDuplicado_lancaConflictException() {
        doThrow(new ConflictException("CPF", "52998224725"))
                .when(uniqueFieldValidator).validar(eq("CPF"), any(), isNull(), any(), any());

        assertThrows(ConflictException.class, () -> pacienteService.registrar(dtoValido()));
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void registrar_comDataNascimentoFutura_lancaIllegalArgumentException() {
        Date dataFutura = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        PacienteRequestDTO dto = new PacienteRequestDTO("Maria Silva", "52998224725",
                "maria@email.com", dataFutura, "Rua A, 1", "21987654321");
        doThrow(new IllegalArgumentException("Data de nascimento inválida"))
                .when(verificarDataNascimento).verificarData(dataFutura);

        assertThrows(IllegalArgumentException.class, () -> pacienteService.registrar(dto));
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void editar_comIdExistente_retornaPacienteAtualizado() {
        Paciente existente = pacienteBuilder(1L);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(pacienteRepository.save(any())).thenReturn(existente);

        PacienteResponseDTO resultado = pacienteService.editar(1L, dtoValido());

        assertThat(resultado).isNotNull();
        verify(pacienteRepository).save(existente);
    }

    @Test
    void editar_comIdInexistente_lancaResourceNotFoundException() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.editar(99L, dtoValido()));
    }

    @Test
    void deletar_comIdExistente_deletaPaciente() {
        Paciente paciente = pacienteBuilder(1L);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        pacienteService.deletar(1L);

        verify(pacienteRepository).delete(paciente);
    }

    @Test
    void deletar_comIdInexistente_lancaResourceNotFoundException() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.deletar(99L));
    }
}
