package com.vitalys.backend.model;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Setter
@Getter
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
}
