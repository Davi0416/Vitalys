package com.vitalys.backend.service.rules;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class VerificarDataNascimento {
    public void verificarData(Date dataNascimento) {
        if (dataNascimento.after(Date.from(Instant.now())))
            throw new IllegalArgumentException("Data de nascimento inválida");
    }
}
