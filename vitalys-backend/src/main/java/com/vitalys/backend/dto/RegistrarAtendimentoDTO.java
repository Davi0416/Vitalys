package com.vitalys.backend.dto;

import java.time.LocalDateTime;

public class RegistrarAtendimentoDTO {
    private Long idPaciente;
    private Long idProfissional;
    private LocalDateTime dataEHoraMarcadas;

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public Long getIdProfissional() { return idProfissional; }
    public void setIdProfissional(Long idProfissional) { this.idProfissional = idProfissional; }

    public LocalDateTime getDataEHoraMarcadas() { return dataEHoraMarcadas; }
    public void setDataEHoraMarcadas(LocalDateTime dataEHoraMarcadas) { this.dataEHoraMarcadas = dataEHoraMarcadas; }
}