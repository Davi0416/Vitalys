package com.vitalys.backend.repository;

import com.vitalys.backend.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    boolean existsByLogin(String login);
    boolean existsByIdProfissional(Long idProfissional);
    Optional<Usuarios> findByLogin(String login);

    boolean existsByLoginAndIdNot(String login, Long id);
    boolean existsByIdProfissionalAndIdNot(Long idProfissional, Long id);
}
