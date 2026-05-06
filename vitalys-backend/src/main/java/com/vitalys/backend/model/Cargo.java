package com.vitalys.backend.model;

import com.vitalys.backend.dto.CargoRequestDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "cargos")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "nivel_acesso")
    private String nivelAcesso;

    public Cargo() {}

    public void atualizarDados(CargoRequestDTO dto) {
        this.nivelAcesso = dto.nivelAcesso();
        this.cargo = dto.cargo();
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCargo() { return cargo; }

    public String getNivelAcesso() { return nivelAcesso; }
}