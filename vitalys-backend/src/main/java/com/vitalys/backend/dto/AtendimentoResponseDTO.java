package com.vitalys.backend.dto;

import java.time.LocalDateTime;

public class AtendimentoResponseDTO {
    private Long id;
    private String nomePaciente;
    private String nomeProfissional;
    private LocalDateTime dataEHoraMarcadas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    public String getNomeProfissional() { return nomeProfissional; }
    public void setNomeProfissional(String nomeProfissional) { this.nomeProfissional = nomeProfissional; }

    public LocalDateTime getDataEHoraMarcadas() { return dataEHoraMarcadas; }
    public void setDataEHoraMarcadas(LocalDateTime dataEHoraMarcadas) { this.dataEHoraMarcadas = dataEHoraMarcadas; }
}