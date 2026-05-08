package com.vitalys.backend.repository;

import com.vitalys.backend.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);

    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByTelefoneAndIdNot(String telefone, Long id);
}
