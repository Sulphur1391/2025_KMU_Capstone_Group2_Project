package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.NotificationDto;
import com.outfit.ai.cloth_app.dto.MessageDto; // [추가] DTO 임포트
import com.outfit.ai.cloth_app.entity.Message;
import com.outfit.ai.cloth_app.service.MessageService;
import com.outfit.ai.cloth_app.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors; // [추가] Stream 사용을 위한 임포트

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final NoticeService noticeService;

    public MessageController(MessageService messageService, NoticeService noticeService) {
        this.messageService = messageService;
        this.noticeService = noticeService;
    }

    // 쪽지 전송 (반환 타입은 Message Entity를 유지해도 되지만, DTO 반환을 권장)
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestParam Long senderId,
                                               @RequestParam Long receiverId,
                                               @RequestParam String content) {
        Message savedMessage = messageService.sendMessage(senderId, receiverId, content);

        // 2️⃣ 실시간 알림 전송 (NoticeService 로직이 변경됨)
        NotificationDto notification = new NotificationDto(
                "MESSAGE",
                savedMessage.getContent(),
                savedMessage.getSender().getId().toString(),
                savedMessage.getReceiver().getId().toString()
        );
        // [수정] NoticeService에서 /user 경로로 알림을 보내도록 로직이 변경되었습니다.
        noticeService.sendMessageNotice(savedMessage.getReceiver().getId().toString(), notification);

        return ResponseEntity.ok(savedMessage);
    }

    // [수정] 받은 쪽지 조회: Message Entity -> MessageDto 리스트 반환
    @GetMapping("/inbox/{receiverId}")
    public ResponseEntity<List<MessageDto>> getReceivedMessages(@PathVariable Long receiverId) {
        List<Message> messages = messageService.getReceivedMessages(receiverId);
        // Entity -> DTO 매핑
        List<MessageDto> dtos = messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // [수정] 보낸 쪽지 조회: Message Entity -> MessageDto 리스트 반환
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<MessageDto>> getSentMessages(@PathVariable Long senderId) {
        List<Message> messages = messageService.getSentMessages(senderId);
        // Entity -> DTO 매핑
        List<MessageDto> dtos = messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // [추가] Message Entity를 MessageDto로 매핑하는 헬퍼 메서드
    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSender().getId());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setSentAt(message.getSentAt());
        return dto;
    }
}