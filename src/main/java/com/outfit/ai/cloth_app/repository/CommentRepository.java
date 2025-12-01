package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 기본 CRUD 제공
}
