package com.vitalys.backend.service;

import com.vitalys.backend.dto.AtendimentoRequestDTO;
import com.vitalys.backend.dto.AtendimentoResponseDTO;
import com.vitalys.backend.exception.BusinessException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.infra.event.AtendimentoCriadoEvent;
import com.vitalys.backend.model.Atendimento;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.AtendimentoRepository;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.repository.ProfissionalRepository;
import com.vitalys.backend.service.strategy.AgendamentoStrategy;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final Map<String, AgendamentoStrategy> strategies;
    private final ApplicationEventPublisher publisher;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                               PacienteRepository pacienteRepository,
                               ProfissionalRepository profissionalRepository,
                               Map<String, AgendamentoStrategy> strategies,
                               ApplicationEventPublisher publisher) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.strategies = strategies;
        this.publisher = publisher;
    }

    private void verificarDadosUnicos(AtendimentoRequestDTO dto, Long idAtual) {
        boolean pacienteExiste = idAtual != null
                ? atendimentoRepository.existsByIdPacienteAndIdNot(dto.idPaciente(), idAtual)
                : atendimentoRepository.existsByIdPaciente(dto.idPaciente());
        if (pacienteExiste) throw new BusinessException("Paciente ja possui um atendimento agendado.");

        boolean horarioExiste = idAtual != null
                ? atendimentoRepository.existsByIdProfissionalAndDataEHoraMarcadasAndIdNot(dto.idProfissional(), dto.dataEHoraMarcadas(), idAtual)
                : atendimentoRepository.existsByIdProfissionalAndDataEHoraMarcadas(dto.idProfissional(), dto.dataEHoraMarcadas());
        if (horarioExiste) throw new BusinessException("Profissional ja possui atendimento nessa data e hora.");
    }

    @Transactional(readOnly = true)
    public List<AtendimentoResponseDTO> findAll() {
        return atendimentoRepository.findAll().stream().map(a -> {
            Paciente paciente = pacienteRepository.findById(a.getIdPaciente())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente", a.getIdPaciente()));
            Profissional profissional = profissionalRepository.findById(a.getIdProfissional())
                    .orElseThrow(() -> new ResourceNotFoundException("Profissional", a.getIdProfissional()));
            return new AtendimentoResponseDTO(a.getId(), profissional.getId(),
                    paciente.getNome(), profissional.getNome(), a.getDataEHoraMarcadas());
        }).toList();
    }

    @Transactional
    public AtendimentoResponseDTO registrar(AtendimentoRequestDTO dto) {
        AgendamentoStrategy strategy = strategies.get(dto.tipo());
        if (strategy == null) throw new BusinessException("Tipo de atendimento invalido: " + dto.tipo());
        strategy.validar(dto);

        Paciente paciente = pacienteRepository.findById(dto.idPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", dto.idPaciente()));
        Profissional profissional = profissionalRepository.findById(dto.idProfissional())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", dto.idProfissional()));
        verificarDadosUnicos(dto, null);

        Atendimento a = Atendimento.builder()
                .idPaciente(dto.idPaciente())
                .idProfissional(dto.idProfissional())
                .dataEHoraMarcadas(dto.dataEHoraMarcadas())
                .build();

        Atendimento saved = atendimentoRepository.save(a);
        publisher.publishEvent(new AtendimentoCriadoEvent(this, saved.getId()));

        return new AtendimentoResponseDTO(saved.getId(), profissional.getId(),
                paciente.getNome(), profissional.getNome(), saved.getDataEHoraMarcadas());
    }

    @Transactional
    public AtendimentoResponseDTO editar(Long id, AtendimentoRequestDTO dto) {
        Atendimento a = atendimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento", id));
        Paciente paciente = pacienteRepository.findById(dto.idPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", dto.idPaciente()));
        Profissional profissional = profissionalRepository.findById(dto.idProfissional())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", dto.idProfissional()));
        verificarDadosUnicos(dto, id);
        a.atualizar(dto);
        atendimentoRepository.save(a);
        return new AtendimentoResponseDTO(a.getId(), profissional.getId(),
                paciente.getNome(), profissional.getNome(), a.getDataEHoraMarcadas());
    }

    @Transactional
    public void deletar(Long id) {
        Atendimento a = atendimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento", id));
        atendimentoRepository.delete(a);
    }
}
