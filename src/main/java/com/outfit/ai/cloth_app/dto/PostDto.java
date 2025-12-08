package com.outfit.ai.cloth_app.dto;

<<<<<<< HEAD
import com.outfit.ai.cloth_app.tables.OutfitCombination;

import java.util.ArrayList;
import java.util.List;

// 게시글 DTO
public class PostDto {
    private String id;
    private String author;
    private String content;
    private String imageUrl;
    private int likeCount;
    private Boolean isShared;
    private List<CommentDto> comments = new ArrayList<>();

    public PostDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsShared() { return isShared; }
    public void setShared(Boolean shared) { isShared = shared; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public void setComments(List<CommentDto> comments) { this.comments = comments; }
    public List<CommentDto> getComments() { return comments; }

    public static PostDto fromEntity(OutfitCombination outfit) {
        PostDto dto = new PostDto();

        dto.setId(outfit.getOutfitId().toString());

        if (outfit.getUserTable() != null) {
            dto.setAuthor(outfit.getUserTable().getUsername());
        }

        dto.setImageUrl(outfit.getAiGenImageUrl());
        dto.setLikeCount(outfit.getLikeCount());

        return dto;
    }
=======
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
>>>>>>> origin/main
}
