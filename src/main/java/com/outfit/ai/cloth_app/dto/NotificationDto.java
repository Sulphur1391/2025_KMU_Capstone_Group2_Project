package com.outfit.ai.cloth_app.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID; // ID 생성을 위한 UUID 임포트 (선택 사항)

/**
 * NotificationDto (수정됨)
 * - Flutter의 NotificationModel 구조와 1:1 매핑되도록 필드명 및 구조 수정
 */

public class NotificationDto {
    private String id;       // 1. 알림 고유 ID (Flutter Model 추가 필드)
    private String type;     // 2. 알림 종류: 'like', 'comment', 'system' 등
    private String title;    // 3. 알림 제목 (Flutter Model 추가 필드)
    private String message;  // 4. 기존 'content' -> 'message'로 변경 (Flutter 필드명 통일)
    private OffsetDateTime createdAt; // 5. 기존 'timestamp' -> 'createdAt'으로 변경 (Flutter 필드명 통일)
    private boolean isRead;  // 6. 읽음 상태 (Flutter Model 추가 필드)
    private Map<String, Object> data; // 7. 추가 데이터 (senderId, targetId 등을 여기에 포함)

    // 기본 생성자 (필수)
    public NotificationDto() {
        this.createdAt = OffsetDateTime.now();
        this.isRead = false;
        // 생성 시 ID를 부여하는 것이 일반적입니다.
        this.id = UUID.randomUUID().toString();
    }

    // 최소 필드 생성자 (옵션: 실제 사용 시 편의를 위해 구성)
    public NotificationDto(String type, String title, String message, Map<String, Object> data) {
        this(); // 기본 생성자 호출
        this.type = type;
        this.title = title;
        this.message = message;
        this.data = data;
    }

    // --- Getter와 Setter ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}