package com.outfit.ai.cloth_app.entity;

import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private int likes = 0;
    private int dislikes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String targetId; // 알림용 대상 ID

    public Comment() {}

    // getter / setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getDislikes() { return dislikes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
}