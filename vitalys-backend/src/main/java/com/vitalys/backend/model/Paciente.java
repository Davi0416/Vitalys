package com.vitalys.backend.model;

import com.vitalys.backend.dto.PacienteRequestDTO;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "data_nascimento")
    private Date dataNascimento;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "telefone")
    private String telefone;

    public void atualizarDados(PacienteRequestDTO dto) {
        this.nome = dto.nome();
        this.cpf = dto.cpf();
        this.email = dto.email();
        this.dataNascimento = dto.dataNascimento();
        this.endereco = dto.endereco();
        this.telefone = dto.telefone();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }

    public String getCpf() { return cpf; }

    public String getEmail() { return email; }

    public Date getDataNascimento() { return dataNascimento; }

    public String getTelefone() { return telefone; }

    public String getEndereco() { return endereco; }

}
