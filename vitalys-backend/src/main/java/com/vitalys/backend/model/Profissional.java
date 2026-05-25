package com.vitalys.backend.model;

import com.vitalys.backend.dto.ProfissionalRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void atualizar(ProfissionalRequestDTO dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.cpf = dto.cpf();
        this.telefone = dto.telefone();
        this.dataNascimento = dto.dataNascimento();
    }
}
