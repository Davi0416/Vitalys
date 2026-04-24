package com.vitalys.backend.model;


import jakarta.persistence.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
    private Date dataEHoraMarcadas;

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

    public Date getDataEHoraMarcadas() {
        return dataEHoraMarcadas;
    }

    public void setDataEHoraMarcadas(Date dataEHoraMarcadas) {
        this.dataEHoraMarcadas = dataEHoraMarcadas;
    }
}
