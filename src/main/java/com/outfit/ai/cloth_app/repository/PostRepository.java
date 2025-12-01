package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본 CRUD (save, findAll, findById, delete 등) 제공
}
