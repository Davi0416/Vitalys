package com.vitalys.backend.model;

import com.vitalys.backend.dto.CalendarioRequestDTO;
import jakarta.persistence.*;

import java.util.Date;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getIdAtendimento() {
        return idAtendimento;
    }
}