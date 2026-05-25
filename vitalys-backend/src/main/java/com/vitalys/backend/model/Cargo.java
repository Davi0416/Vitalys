package com.vitalys.backend.model;

import com.vitalys.backend.dto.CargoRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void atualizar(CargoRequestDTO dto) {
        this.cargo = dto.cargo();
        this.nivelAcesso = dto.nivelAcesso();
    }
}
