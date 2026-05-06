package com.vitalys.backend.model;



import com.vitalys.backend.dto.AtendimentoRequestDTO;
import com.vitalys.backend.repository.AtendimentoRepository;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "atendimento")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente")
    private Long idPaciente;

    @Column(name = "id_profissional")
    private Long idProfissional;

    @Column(name = "data_e_hora_marcadas")
    private LocalDateTime dataEHoraMarcadas;

    public void atualizarDados(AtendimentoRequestDTO dto) {
        this.dataEHoraMarcadas = dto.dataEHoraMarcadas();
        this.idPaciente = dto.idPaciente();
        this.idProfissional = dto.idProfissional();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Long getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(Long idProfissional) {
        this.idProfissional = idProfissional;
    }

    public LocalDateTime getDataEHoraMarcadas() {
        return dataEHoraMarcadas;
    }

    public void setDataEHoraMarcadas(LocalDateTime dataEHoraMarcadas) {
        this.dataEHoraMarcadas = dataEHoraMarcadas;
    }
}
