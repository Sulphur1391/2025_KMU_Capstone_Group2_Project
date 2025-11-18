package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.tables.OutfitCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutfitCombinationRepository extends JpaRepository<OutfitCombination, UUID> {
    List<OutfitCombination> findByIsSharedTrueOrderByCreatedAtDesc();
}
