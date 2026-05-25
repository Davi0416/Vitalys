package com.vitalys.backend.model;

import com.vitalys.backend.dto.PacienteRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void atualizar(PacienteRequestDTO dto) {
        this.nome = dto.nome();
        this.cpf = dto.cpf();
        this.email = dto.email();
        this.dataNascimento = dto.dataNascimento();
        this.endereco = dto.endereco();
        this.telefone = dto.telefone();
    }
}
