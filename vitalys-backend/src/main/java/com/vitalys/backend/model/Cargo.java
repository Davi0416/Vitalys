package com.vitalys.backend.model;

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

    public Cargo(Long id, String cargo, String nivelAcesso){
        this.id = id;
        this.cargo = cargo;
        this.nivelAcesso = nivelAcesso;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }
}