package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.MessageDto;
import com.outfit.ai.cloth_app.dto.NotificationDto;
import com.outfit.ai.cloth_app.service.MessageService;
import com.outfit.ai.cloth_app.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap; // Map 사용을 위해 추가
import java.util.List;
import java.util.Map; // Map 사용을 위해 추가
import java.util.UUID;

// 쪽지 컨트롤러
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final NoticeService noticeService;

    // 생성자 주입
    public MessageController(MessageService messageService, NoticeService noticeService) {
        this.messageService = messageService;
        this.noticeService = noticeService;
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }

        String userIdStr = authentication.getName();
        if (userIdStr == null) {
            throw new RuntimeException("인증된 사용자 ID가 유효하지 않습니다.");
        }
        return UUID.fromString(userIdStr);
    }

    // 메시지 보내기
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto message) {
        UUID senderId = getAuthenticatedUserId();

        // 1. 메시지 저장 및 전송 로직 수행
        MessageDto sentMessage = messageService.sendMessage(senderId, message);

        // 2. 메시지 수신자에게 실시간 알림 전송 (NoticeService 사용)
        UUID receiverId = sentMessage.getReceiverId();

        // content는 메시지 내용을 50자 이내로 요약
        String fullContent = sentMessage.getContent();
        String briefContent = fullContent.substring(0, Math.min(fullContent.length(), 50));

        // 🔔 [주의] 발신자 이름은 MessageService에서 가져와야 함 (가정)
        // String senderName = messageService.getSenderName(senderId);

        // -----------------------------------------------------------------
        // ⭐ [핵심 수정 부분] 수정된 NotificationDto 구조에 맞춥니다.
        // -----------------------------------------------------------------

        // 2-1. data 맵 생성 (Flutter Model의 data 필드에 들어갈 추가 정보)
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("senderId", senderId.toString());
        // 쪽지 알림이므로, 관련 채팅방 ID나 메시지 ID를 추가하면 이동에 편리합니다.
        // notificationData.put("messageId", sentMessage.getId().toString());

        // 2-2. NotificationDto 객체 생성 (기본 생성자 사용)
        NotificationDto notification = new NotificationDto();

        // 2-3. 필드 채우기 (Flutter Model: id, type, title, message, data)
        // ID, createdAt, isRead는 DTO 생성자에서 자동 생성됨

        // 🔔 알림 타입 설정 (Flutter Enum과의 통일성을 위해 'system' 사용 또는 'message' 타입을 추가해야 함)
        notification.setType("system");

        // 🔔 title 설정 (예시: 실제 발신자 이름으로 교체 필요)
        notification.setTitle("새로운 쪽지가 도착했습니다.");

        notification.setMessage(briefContent + (fullContent.length() > 50 ? "..." : "")); // Flutter의 'message' 필드

        notification.setData(notificationData); // Flutter의 'data' 필드에 관련 ID 삽입

        // -----------------------------------------------------------------

        // NoticeService의 sendMessageNotice 호출
        noticeService.sendMessageNotice(receiverId, notification);

        return ResponseEntity.ok(sentMessage);
    }

    // 쪽지함 보기
    @GetMapping("/inbox")
    public ResponseEntity<List<MessageDto>> getInbox() {
        UUID receiverId = getAuthenticatedUserId();
        List<MessageDto> inbox = messageService.getInbox(receiverId);
        return ResponseEntity.ok(inbox);
    }

    // 보낸 메시지
    @GetMapping("/sent")
    public ResponseEntity<List<MessageDto>> getSent() {
        UUID senderId = getAuthenticatedUserId();
        List<MessageDto> sent = messageService.getSent(senderId);
        return ResponseEntity.ok(sent);
    }
}