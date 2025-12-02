package com.outfit.ai.cloth_app.dto;

import java.time.LocalDateTime;

public class MessageDto {
    private Long id;
    // FIXED: sender/receiver를 String으로 쓰지 않고 userId(Long)로 참조
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime sentAt = LocalDateTime.now();

    public MessageDto() {}

    public MessageDto(Long id, Long senderId, Long receiverId, String content) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
