package com.vitalys.backend.repository;

import com.vitalys.backend.model.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarioRepository extends JpaRepository<Calendario, Long> {
}
