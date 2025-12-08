package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.MessageDto;
<<<<<<< HEAD
import com.outfit.ai.cloth_app.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// 쪽지 컨트롤러
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        return UUID.fromString(authentication.getName());
    }

    // 메시지 보내기
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto message) {
        UUID senderId = getAuthenticatedUserId();

        MessageDto sentMessage = messageService.sendMessage(senderId, message);
        return ResponseEntity.ok(sentMessage);
    }

    // 쪽지함 보기
    @GetMapping("/inbox")
    public ResponseEntity<List<MessageDto>> getInbox() {
        UUID receiverId = getAuthenticatedUserId();
        List<MessageDto> inbox = messageService.getInbox(receiverId);
        return ResponseEntity.ok(inbox);
    }

    // 받은 메시지
    @GetMapping("/sent")
    public ResponseEntity<List<MessageDto>> getSent() {
        UUID senderId = getAuthenticatedUserId();
        List<MessageDto> sent = messageService.getSent(senderId);
        return ResponseEntity.ok(sent);
=======
import com.outfit.ai.cloth_app.dto.NotificationDto;
import com.outfit.ai.cloth_app.entity.Message;
import com.outfit.ai.cloth_app.service.MessageService;
import com.outfit.ai.cloth_app.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MessageController
 * - 엔티티 대신 MessageDto 반환
 * - 메시지 전송 시 알림 자동 발송
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final NoticeService noticeService;

    @PostMapping
    public MessageDto sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content
    ) {
        Message saved = messageService.sendMessage(senderId, receiverId, content);

        // 🔔 알림 전송
        NotificationDto notification = new NotificationDto(
                "MESSAGE",
                saved.getContent(),
                senderId.toString(),
                receiverId.toString()
        );
        noticeService.sendMessageNotice(receiverId, notification);

        return MessageDto.from(saved);
    }

    @GetMapping("/received")
    public List<MessageDto> getReceived(@RequestParam Long receiverId) {
        return messageService.getReceivedMessages(receiverId)
                .stream().map(MessageDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/sent")
    public List<MessageDto> getSent(@RequestParam Long senderId) {
        return messageService.getSentMessages(senderId)
                .stream().map(MessageDto::from)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
>>>>>>> origin/main
    }
}
