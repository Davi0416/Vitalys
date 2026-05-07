package com.vitalys.backend.repository;

import com.vitalys.backend.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    boolean existsByLogin(String login);
    boolean existsByIdProfissional(Long idProfissional);
}
