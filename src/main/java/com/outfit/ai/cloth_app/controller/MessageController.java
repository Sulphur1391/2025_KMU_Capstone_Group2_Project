package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.MessageDto;
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
    }
}
