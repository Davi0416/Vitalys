package com.vitalys.backend.service;

import com.vitalys.backend.dto.AtendimentoRequestDTO;
import com.vitalys.backend.dto.AtendimentoResponseDTO;
import com.vitalys.backend.exception.BusinessException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Atendimento;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.AtendimentoRepository;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.repository.ProfissionalRepository;
import com.vitalys.backend.service.strategy.AgendamentoStrategy;
import com.vitalys.backend.service.strategy.ConsultaStrategy;
import com.vitalys.backend.service.strategy.RetornoStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtendimentoServiceTest {

    @Mock AtendimentoRepository atendimentoRepository;
    @Mock PacienteRepository pacienteRepository;
    @Mock ProfissionalRepository profissionalRepository;
    @Mock ApplicationEventPublisher publisher;

    private AtendimentoService atendimentoService;

    private static final LocalDateTime DATA_HORA = LocalDateTime.of(2026, 8, 1, 10, 0);

    @BeforeEach
    void setUp() {
        Map<String, AgendamentoStrategy> strategies = new HashMap<>();
        strategies.put("consulta", new ConsultaStrategy());
        strategies.put("retorno", new RetornoStrategy());
        atendimentoService = new AtendimentoService(
                atendimentoRepository, pacienteRepository, profissionalRepository, strategies, publisher);
    }

    private AtendimentoRequestDTO dtoValido() {
        return new AtendimentoRequestDTO(1L, 2L, DATA_HORA, "consulta");
    }

    @Test
    void registrar_comDadosValidos_retornaAtendimentoResponseDTO() {
        Paciente paciente = Paciente.builder().id(1L).nome("Maria").build();
        Profissional profissional = Profissional.builder().id(2L).nome("Dr. Carlos").build();
        Atendimento salvo = Atendimento.builder().id(10L).idPaciente(1L).idProfissional(2L).dataEHoraMarcadas(DATA_HORA).build();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(profissional));
        when(atendimentoRepository.existsByIdPaciente(1L)).thenReturn(false);
        when(atendimentoRepository.existsByIdProfissionalAndDataEHoraMarcadas(2L, DATA_HORA)).thenReturn(false);
        when(atendimentoRepository.save(any())).thenReturn(salvo);

        AtendimentoResponseDTO resultado = atendimentoService.registrar(dtoValido());

        assertThat(resultado.id()).isEqualTo(10L);
        assertThat(resultado.nomePaciente()).isEqualTo("Maria");
        assertThat(resultado.nomeProfissional()).isEqualTo("Dr. Carlos");
        verify(publisher).publishEvent(any());
    }

    @Test
    void registrar_comTipoInvalido_lancaBusinessException() {
        AtendimentoRequestDTO dto = new AtendimentoRequestDTO(1L, 2L, DATA_HORA, "tipo_invalido");

        assertThrows(BusinessException.class, () -> atendimentoService.registrar(dto));
        verify(atendimentoRepository, never()).save(any());
    }

    @Test
    void registrar_comPacienteJaAgendado_lancaBusinessException() {
        Paciente paciente = Paciente.builder().id(1L).nome("Maria").build();
        Profissional profissional = Profissional.builder().id(2L).nome("Dr. Carlos").build();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(profissional));
        when(atendimentoRepository.existsByIdPaciente(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> atendimentoService.registrar(dtoValido()));
        verify(atendimentoRepository, never()).save(any());
    }

    @Test
    void registrar_comHorarioOcupado_lancaBusinessException() {
        Paciente paciente = Paciente.builder().id(1L).nome("Maria").build();
        Profissional profissional = Profissional.builder().id(2L).nome("Dr. Carlos").build();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(profissional));
        when(atendimentoRepository.existsByIdPaciente(1L)).thenReturn(false);
        when(atendimentoRepository.existsByIdProfissionalAndDataEHoraMarcadas(2L, DATA_HORA)).thenReturn(true);

        assertThrows(BusinessException.class, () -> atendimentoService.registrar(dtoValido()));
    }

    @Test
    void deletar_comIdExistente_deletaAtendimento() {
        Atendimento atendimento = Atendimento.builder().id(1L).build();
        when(atendimentoRepository.findById(1L)).thenReturn(Optional.of(atendimento));

        atendimentoService.deletar(1L);

        verify(atendimentoRepository).delete(atendimento);
    }

    @Test
    void deletar_comIdInexistente_lancaResourceNotFoundException() {
        when(atendimentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> atendimentoService.deletar(99L));
    }

    @Test
    void findAll_retornaListaDeAtendimentos() {
        Atendimento a = Atendimento.builder().id(1L).idPaciente(1L).idProfissional(2L).dataEHoraMarcadas(DATA_HORA).build();
        Paciente paciente = Paciente.builder().id(1L).nome("Maria").build();
        Profissional profissional = Profissional.builder().id(2L).nome("Dr. Carlos").build();

        when(atendimentoRepository.findAll()).thenReturn(List.of(a));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(profissionalRepository.findById(2L)).thenReturn(Optional.of(profissional));

        List<AtendimentoResponseDTO> resultado = atendimentoService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nomePaciente()).isEqualTo("Maria");
    }
}
