package com.vitalys.backend.model;

import com.vitalys.backend.dto.RegistrarPacienteDTO;
import com.vitalys.backend.dto.RegistrarProfissionalDTO;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "profissionais")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "data_nascimento")
    private Date dataNascimento;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "telefone")
    private String telefone;

    public Profissional registrar(RegistrarProfissionalDTO registrarProfissionalDTO) {
        this.nome = registrarProfissionalDTO.getNome();
        this.email = registrarProfissionalDTO.getCpf();
        this.dataNascimento = registrarProfissionalDTO.getDataNascimento();
        this.cpf = registrarProfissionalDTO.getCpf();
        this.telefone = registrarProfissionalDTO.getTelefone();
        return this;
    }

    public void atualizar(RegistrarProfissionalDTO atualizarProfissional) {
        this.nome = atualizarProfissional.getNome();
        this.cpf = atualizarProfissional.getCpf();
        this.email = atualizarProfissional.getEmail();
        this.dataNascimento = atualizarProfissional.getDataNascimento();
        this.telefone = atualizarProfissional.getTelefone();
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
    }

    public Profissional() {

    }
}
