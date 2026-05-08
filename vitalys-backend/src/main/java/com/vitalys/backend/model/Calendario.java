package com.vitalys.backend.model;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "calendario")
public class Calendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data")
    private Date data;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "id_atendimento")
    private Long idAtendimento;

    public void atualizarDados(CalendarioRequestDTO dto) {
        this.data = dto.data();
        this.nome = dto.nome();
        this.tipo = dto.tipo();
        this.idAtendimento = dto.idAtendimento();
    }
}