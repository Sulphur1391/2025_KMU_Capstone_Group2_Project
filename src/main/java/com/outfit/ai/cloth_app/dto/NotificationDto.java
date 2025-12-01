package com.outfit.ai.cloth_app.dto;

public class NotificationDto {
    private String type;       // COMMENT, MESSAGE 등
    private String content;
    private String senderId;
    private String targetId;

    // 생성자, getter/setter
    public NotificationDto() {}
    public NotificationDto(String type, String content, String senderId, String targetId) {
        this.type = type;
        this.content = content;
        this.senderId = senderId;
        this.targetId = targetId;
    }
    // getter, setter 생략
}
