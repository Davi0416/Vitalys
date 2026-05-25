package com.vitalys.backend.infra.event;

import org.springframework.context.ApplicationEvent;

public class AtendimentoCriadoEvent extends ApplicationEvent {

    private final Long atendimentoId;

    public AtendimentoCriadoEvent(Object source, Long atendimentoId) {
        super(source);
        this.atendimentoId = atendimentoId;
    }

    public Long getAtendimentoId() {
        return atendimentoId;
    }
}
