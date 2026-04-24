package com.vitalys.backend.repository;

import com.vitalys.backend.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
}
