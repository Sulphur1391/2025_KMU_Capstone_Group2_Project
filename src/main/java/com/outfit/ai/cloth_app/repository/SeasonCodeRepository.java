package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.tables.SeasonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SeasonCodeRepository extends JpaRepository<SeasonCodeRepository, UUID> {
    Optional<SeasonCode> findBySeasonName(String name);
}
