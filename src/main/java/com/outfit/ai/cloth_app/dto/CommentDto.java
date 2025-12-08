package com.outfit.ai.cloth_app.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private Integer likes;
    private Integer dislikes;
    private Long authorId;

    // 🔥 수정됨: String → Long
    private Long targetId;
}
