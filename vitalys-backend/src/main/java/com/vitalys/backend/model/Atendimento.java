package com.vitalys.backend.model;



import com.vitalys.backend.dto.AtendimentoRequestDTO;
import com.vitalys.backend.repository.AtendimentoRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
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
}
