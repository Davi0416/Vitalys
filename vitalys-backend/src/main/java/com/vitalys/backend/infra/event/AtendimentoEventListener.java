package com.vitalys.backend.infra.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AtendimentoEventListener {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoEventListener.class);

    @EventListener
    public void onAtendimentoCriado(AtendimentoCriadoEvent event) {
        log.info("[Event] Novo atendimento criado — ID: {}", event.getAtendimentoId());
    }
}
