package com.vitalys.backend.service.strategy;

import com.vitalys.backend.dto.AtendimentoRequestDTO;
import org.springframework.stereotype.Component;

@Component("retorno")
public class RetornoStrategy implements AgendamentoStrategy {

    @Override
    public void validar(AtendimentoRequestDTO dto) {
        if (dto.dataEHoraMarcadas() == null) {
            throw new IllegalArgumentException("Retorno requer data e hora.");
        }
    }
}
