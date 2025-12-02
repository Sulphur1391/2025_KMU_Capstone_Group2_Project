package com.outfit.ai.cloth_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private Long authorId;      // 작성자 ID
    private String authorName;  // 작성자 이름
    private String title;
    private String content;

    private int likeCount;      // 🔹 Entity likeCount와 매칭
    private int dislikeCount;   // 🔹 Entity dislikeCount와 매칭
    private String createdAt;   // 🔹 Entity createdAt과 매칭
}
