package com.capston.backend.dto;

public class CommentDto {
    private Long id;
    private String author;
    private String content;
    private int likes = 0;
    private int dislikes = 0;

    public CommentDto() {}

    public CommentDto(Long id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getDislikes() { return dislikes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }
}
