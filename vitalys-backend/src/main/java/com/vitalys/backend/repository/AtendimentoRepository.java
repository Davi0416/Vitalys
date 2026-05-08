package com.vitalys.backend.repository;

import com.vitalys.backend.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    boolean existsByIdPaciente(Long idPaciente);
    boolean existsByIdProfissionalAndDataEHoraMarcadas(Long idProfissional, LocalDateTime dataEHoraMarcadas);

    boolean existsByIdPacienteAndIdNot(Long idPaciente, Long id);
    boolean existsByIdProfissionalAndDataEHoraMarcadasAndIdNot(Long idProfissional, LocalDateTime dataEHoraMarcadas, Long id);
}
