package com.outfit.ai.cloth_app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private int likes;
    private int dislikes;
    // FIXED: authorId를 String -> Long으로 변경하여 Entity(User.id)와 타입 정합성 맞춤
    private Long authorId;
    private String targetId;
}
