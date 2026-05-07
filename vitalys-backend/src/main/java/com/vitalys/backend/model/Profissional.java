package com.vitalys.backend.model;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.br.CPF;

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

    @CPF
    @Column(name = "cpf")
    private String cpf;

    @Column(name = "telefone")
    private String telefone;

    public void atualizarDados(ProfissionalRequestDTO dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.dataNascimento =  dto.dataNascimento();
        this.cpf =  dto.cpf();
        this.telefone = dto.telefone();
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

    public String getEmail() {
        return email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }
}
