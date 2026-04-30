package com.vitalys.backend.controller;

import com.vitalys.backend.dto.AtendimentoResponseDTO;
import com.vitalys.backend.dto.RegistrarAtendimentoDTO;
import com.vitalys.backend.model.Atendimento;
import com.vitalys.backend.model.Paciente;
import com.vitalys.backend.model.Profissional;
import com.vitalys.backend.repository.AtendimentoRepository;
import com.vitalys.backend.repository.PacienteRepository;
import com.vitalys.backend.repository.ProfissionalRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/vitalys")
public class AtendimentoController {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @PostMapping(path = "/atendimentos")
    public AtendimentoResponseDTO create(@RequestBody RegistrarAtendimentoDTO dto) {
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

    @GetMapping(path = "/atendimentos")
    public @ResponseBody List<Atendimento> findAll() {
        return atendimentoRepository.findAll();
    }

    @DeleteMapping(path = "/atendimentos/{id}")
    public void deleteAtendimento(@PathVariable Long id){
        atendimentoRepository.deleteById(id);
    }

    @PutMapping(path = "/atendimentos/{id}")
    public Atendimento updateAtendimento(@PathVariable Long id, @RequestBody @Valid RegistrarAtendimentoDTO atendimento){
        Atendimento atendimentoExistente = atendimentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
        atendimentoExistente.setDataEHoraMarcadas(atendimento.getDataEHoraMarcadas());
        atendimentoExistente.setIdPaciente(atendimento.getIdPaciente());
        atendimentoExistente.setIdProfissional(atendimento.getIdProfissional());
        return atendimentoRepository.save(atendimentoExistente);
    }
}