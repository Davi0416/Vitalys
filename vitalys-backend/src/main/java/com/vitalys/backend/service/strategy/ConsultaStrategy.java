package com.vitalys.backend.service.strategy;

import com.vitalys.backend.dto.AtendimentoRequestDTO;
import org.springframework.stereotype.Component;

@Component("consulta")
public class ConsultaStrategy implements AgendamentoStrategy {

    @Override
    public void validar(AtendimentoRequestDTO dto) {
        if (dto.idPaciente() == null || dto.idProfissional() == null) {
            throw new IllegalArgumentException("Consulta requer paciente e profissional.");
        }
    }
}
