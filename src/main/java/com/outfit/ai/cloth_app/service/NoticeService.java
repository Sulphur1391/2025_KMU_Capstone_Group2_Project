package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.dto.NotificationDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class
NoticeService {

    private final SimpMessagingTemplate messagingTemplate;

    public NoticeService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendCommentNotice(String targetUserId, NotificationDto notification) {
        messagingTemplate.convertAndSend("/topic/notice/" + targetUserId, notification);
    }

    public void sendMessageNotice(String targetUserId, NotificationDto notification) {
        messagingTemplate.convertAndSend("/topic/notice/" + targetUserId, notification);
    }

    // 필요 시 다른 알림 유형도 추가 가능
}
