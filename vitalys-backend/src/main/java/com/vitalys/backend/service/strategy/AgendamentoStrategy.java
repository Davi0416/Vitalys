package com.vitalys.backend.service.strategy;

import com.vitalys.backend.dto.AtendimentoRequestDTO;

public interface AgendamentoStrategy {
    void validar(AtendimentoRequestDTO dto);
}
