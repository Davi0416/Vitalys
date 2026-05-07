package com.vitalys.backend.service;

import com.vitalys.backend.dto.AtendimentoRequestDTO;
import com.vitalys.backend.dto.AtendimentoResponseDTO;
import com.vitalys.backend.exception.BusinessException;
import com.vitalys.backend.exception.ResourceNotFoundException;
import com.vitalys.backend.model.Atendimento;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.AtendimentoRepository;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                               PacienteRepository pacienteRepository,
                               ProfissionalRepository profissionalRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
    }

    private void verificarDadosUnicos(AtendimentoRequestDTO dto) {
        if (atendimentoRepository.existsByIdPaciente(dto.idPaciente())){
            throw new BusinessException("Paciente já possui um atendimento agendado.");
        }
        if (atendimentoRepository.existsByIdProfissionalAndDataEHoraMarcadas(dto.idProfissional(), dto.dataEHoraMarcadas())) {
            throw new BusinessException("Profissional já possui atendimento nessa data e hora.");
        }
    }

    public AtendimentoResponseDTO registrar(AtendimentoRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.idPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", dto.idPaciente()));
        Profissional profissional = profissionalRepository.findById(dto.idProfissional())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", dto.idProfissional()));
        verificarDadosUnicos(dto);
        Atendimento a = new Atendimento();
        a.atualizarDados(dto);
        atendimentoRepository.save(a);

        return new AtendimentoResponseDTO(a.getId(), profissional.getId(), paciente.getNome(), profissional.getNome(), a.getDataEHoraMarcadas());
    }

    public List<AtendimentoResponseDTO> findAll() {
        return atendimentoRepository.findAll().stream().map(a -> {
            Paciente paciente = pacienteRepository.findById(a.getIdPaciente())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente", a.getIdPaciente()));
            Profissional profissional = profissionalRepository.findById(a.getIdProfissional())
                    .orElseThrow(() -> new ResourceNotFoundException("Profissional", a.getIdProfissional()));
            return new AtendimentoResponseDTO(a.getId(), profissional.getId(), paciente.getNome(), profissional.getNome(), a.getDataEHoraMarcadas());
        }).toList();
    }

    public AtendimentoResponseDTO editar(Long id, AtendimentoRequestDTO dto) {
        Atendimento a = atendimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento", id));
        Paciente paciente = pacienteRepository.findById(dto.idPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", dto.idPaciente()));
        Profissional profissional = profissionalRepository.findById(dto.idProfissional())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", dto.idProfissional()));
        verificarDadosUnicos(dto);
        a.atualizarDados(dto);
        atendimentoRepository.save(a);

        return new AtendimentoResponseDTO(a.getId(), profissional.getId(), paciente.getNome(), profissional.getNome(), a.getDataEHoraMarcadas());
    }

    public void deletar(Long id){
        Atendimento atendimento = atendimentoRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Atendimento", id));
        atendimentoRepository.delete(atendimento);
    }
}
