package com.vitalys.backend.service.rules;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class VerificarDataNascimentoTest {

    private final VerificarDataNascimento verificador = new VerificarDataNascimento();

    @Test
    void verificarData_comDataPassada_naoLancaExcecao() {
        Date dataPassada = Date.from(Instant.now().minus(365 * 25, ChronoUnit.DAYS));
        assertDoesNotThrow(() -> verificador.verificarData(dataPassada));
    }

    @Test
    void verificarData_comDataFutura_lancaIllegalArgumentException() {
        Date dataFutura = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        assertThrows(IllegalArgumentException.class, () -> verificador.verificarData(dataFutura));
    }
}
