package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.tables.CategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryCode, Integer> {
    Optional<CategoryCode> findByCategoryName(String name);
}
