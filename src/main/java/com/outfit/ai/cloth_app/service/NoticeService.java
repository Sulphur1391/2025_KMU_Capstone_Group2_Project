package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.dto.NotificationDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    private final SimpMessagingTemplate messagingTemplate;

    public NoticeService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // [수정] 댓글 알림: /topic 대신 convertAndSendToUser를 사용하여 1:1 개인 알림으로 전송
    public void sendCommentNotice(String targetUserId, NotificationDto notification) {
        // 경로: /user/{targetUserId}/queue/notice
        // 클라이언트는 '/user/queue/notice' 경로를 구독합니다.
        messagingTemplate.convertAndSendToUser(
                targetUserId,
                "/queue/notice",
                notification
        );
    }

    // [수정] 쪽지 알림: /topic 대신 convertAndSendToUser를 사용하여 1:1 개인 알림으로 전송
    public void sendMessageNotice(String targetUserId, NotificationDto notification) {
        // 경로: /user/{targetUserId}/queue/notice
        messagingTemplate.convertAndSendToUser(
                targetUserId,
                "/queue/notice",
                notification
        );
    }

    // 필요 시 다른 알림 유형도 추가 가능
}