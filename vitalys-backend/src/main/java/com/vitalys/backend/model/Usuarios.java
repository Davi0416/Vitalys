package com.vitalys.backend.model;

import com.vitalys.backend.dto.UsuariosRequestDTO;
import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "senha")
    private String senha;

    @Column(name = "id_cargo")
    private Long idCargo;

    @Column(name = "id_profissional")
    private Long idProfissional;

    @Column(name = "ativo")
    private Boolean ativo;

    public void atualizarDados(UsuariosRequestDTO dto) {
        this.login = dto.login();
        this.senha = dto.senha();
        this.idCargo = dto.idCargo();
        this.idProfissional = dto.idProfissional();
        this.ativo = dto.ativo();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Long getIdCargo() {
        return idCargo;
    }

    public Long getIdProfissional() {
        return idProfissional;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
