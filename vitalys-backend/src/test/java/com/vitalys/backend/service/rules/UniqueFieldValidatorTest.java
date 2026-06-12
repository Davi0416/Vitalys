package com.vitalys.backend.service.rules;

import com.vitalys.backend.exception.ConflictException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniqueFieldValidatorTest {

    private final UniqueFieldValidator validator = new UniqueFieldValidator();

    @Test
    void validar_criacao_semConflito_naoLancaExcecao() {
        assertDoesNotThrow(() ->
            validator.validar("CPF", "12345678900", null, () -> false, (v, id) -> false)
        );
    }

    @Test
    void validar_criacao_comConflito_lancaConflictException() {
        assertThrows(ConflictException.class, () ->
            validator.validar("CPF", "12345678900", null, () -> true, (v, id) -> false)
        );
    }

    @Test
    void validar_edicao_semConflito_naoLancaExcecao() {
        assertDoesNotThrow(() ->
            validator.validar("email", "a@b.com", 1L, () -> true, (v, id) -> false)
        );
    }

    @Test
    void validar_edicao_comConflito_lancaConflictException() {
        assertThrows(ConflictException.class, () ->
            validator.validar("email", "a@b.com", 1L, () -> true, (v, id) -> true)
        );
    }
}
