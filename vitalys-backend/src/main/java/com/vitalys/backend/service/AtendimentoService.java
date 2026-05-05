package com.vitalys.backend.service;

import com.vitalys.backend.dto.AtendimentoResponseDTO;
import com.vitalys.backend.dto.RegistrarAtendimentoDTO;
import com.vitalys.backend.model.Atendimento;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.AtendimentoRepository;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public AtendimentoResponseDTO registrar(RegistrarAtendimentoDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        Profissional profissional = profissionalRepository.findById(dto.getIdProfissional())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        Atendimento atendimento = new Atendimento();
        atendimento.setIdPaciente(dto.getIdPaciente());
        atendimento.setIdProfissional(dto.getIdProfissional());
        atendimento.setDataEHoraMarcadas(dto.getDataEHoraMarcadas());
        atendimentoRepository.save(atendimento);

        AtendimentoResponseDTO response = new AtendimentoResponseDTO();
        response.setId(atendimento.getId());
        response.setNomePaciente(paciente.getNome());
        response.setNomeProfissional(profissional.getNome());
        response.setDataEHoraMarcadas(atendimento.getDataEHoraMarcadas());
        return response;
    }

    public List<AtendimentoResponseDTO> findAll() {
        return atendimentoRepository.findAll().stream().map(a -> {
            Paciente paciente = pacienteRepository.findById(a.getIdPaciente())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            Profissional profissional = profissionalRepository.findById(a.getIdProfissional())
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
            AtendimentoResponseDTO dto = new AtendimentoResponseDTO();
            dto.setId(a.getId());
            dto.setNomePaciente(paciente.getNome());
            dto.setNomeProfissional(profissional.getNome());
            dto.setDataEHoraMarcadas(a.getDataEHoraMarcadas());
            return dto;
        }).collect(Collectors.toList());
    }

    public AtendimentoResponseDTO editar(Long id, RegistrarAtendimentoDTO dto) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        Profissional profissional = profissionalRepository.findById(dto.getIdProfissional())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        atendimento.setIdPaciente(dto.getIdPaciente());
        atendimento.setIdProfissional(dto.getIdProfissional());
        atendimento.setDataEHoraMarcadas(dto.getDataEHoraMarcadas());
        atendimentoRepository.save(atendimento);

        AtendimentoResponseDTO response = new AtendimentoResponseDTO();
        response.setId(atendimento.getId());
        response.setNomePaciente(paciente.getNome());
        response.setNomeProfissional(profissional.getNome());
        response.setDataEHoraMarcadas(atendimento.getDataEHoraMarcadas());
        return response;
    }
}
