package com.vitalys.backend.model;

import com.vitalys.backend.dto.AtendimentoRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void atualizar(AtendimentoRequestDTO dto) {
        this.idPaciente = dto.idPaciente();
        this.idProfissional = dto.idProfissional();
        this.dataEHoraMarcadas = dto.dataEHoraMarcadas();
    }
}
