package com.vitalys.backend.service;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import com.vitalys.backend.dto.ProfissionalResponseDTO;
import com.vitalys.backend.exception.ConflictException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.ProfissionalRepository;
import com.vitalys.backend.service.rules.UniqueFieldValidator;
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
class ProfissionalServiceTest {

    @Mock ProfissionalRepository profissionalRepository;
    @Mock UniqueFieldValidator uniqueFieldValidator;
    @InjectMocks ProfissionalService profissionalService;

    private static final Date DATA_VALIDA = Date.from(Instant.now().minus(365 * 30L, ChronoUnit.DAYS));

    private ProfissionalRequestDTO dtoValido() {
        return new ProfissionalRequestDTO("Carlos Souza", "carlos@email.com",
                "52998224725", "21912345678", DATA_VALIDA);
    }

    private Profissional profissionalBuilder(Long id) {
        return Profissional.builder()
                .id(id).nome("Carlos Souza").email("carlos@email.com")
                .cpf("52998224725").telefone("21912345678").dataNascimento(DATA_VALIDA)
                .build();
    }

    @Test
    void findAll_retornaListaDeProfissionais() {
        when(profissionalRepository.findAll()).thenReturn(List.of(profissionalBuilder(1L)));

        List<ProfissionalResponseDTO> resultado = profissionalService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nome()).isEqualTo("Carlos Souza");
    }

    @Test
    void registrar_comDadosValidos_retornaProfissionalResponseDTO() {
        Profissional salvo = profissionalBuilder(1L);
        when(profissionalRepository.save(any())).thenReturn(salvo);

        ProfissionalResponseDTO resultado = profissionalService.registrar(dtoValido());

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Carlos Souza");
        verify(profissionalRepository).save(any(Profissional.class));
    }

    @Test
    void registrar_comCpfDuplicado_lancaConflictException() {
        doThrow(new ConflictException("CPF", "52998224725"))
                .when(uniqueFieldValidator).validar(eq("CPF"), any(), isNull(), any(), any());

        assertThrows(ConflictException.class, () -> profissionalService.registrar(dtoValido()));
        verify(profissionalRepository, never()).save(any());
    }

    @Test
    void editar_comIdExistente_retornaProfissionalAtualizado() {
        Profissional existente = profissionalBuilder(1L);
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(profissionalRepository.save(any())).thenReturn(existente);

        ProfissionalResponseDTO resultado = profissionalService.editar(1L, dtoValido());

        assertThat(resultado).isNotNull();
        verify(profissionalRepository).save(existente);
    }

    @Test
    void editar_comIdInexistente_lancaResourceNotFoundException() {
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profissionalService.editar(99L, dtoValido()));
    }

    @Test
    void deletar_comIdExistente_deletaProfissional() {
        Profissional profissional = profissionalBuilder(1L);
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        profissionalService.deletar(1L);

        verify(profissionalRepository).delete(profissional);
    }

    @Test
    void deletar_comIdInexistente_lancaResourceNotFoundException() {
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profissionalService.deletar(99L));
    }
}
