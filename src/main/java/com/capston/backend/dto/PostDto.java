package com.capston.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class PostDto {
    private Long id;
    private String title;   // ← 새로 추가
    private String author;
    private String content;
    private List<CommentDto> comments = new ArrayList<>();

    public PostDto() {}

    public PostDto(Long id, String title, String author, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }  // ← 새로 추가
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<CommentDto> getComments() { return comments; }
    public void setComments(List<CommentDto> comments) { this.comments = comments; }
}
