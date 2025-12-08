package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.MessageDto;
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
    }
}
