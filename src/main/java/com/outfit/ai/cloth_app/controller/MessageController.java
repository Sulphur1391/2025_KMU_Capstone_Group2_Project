package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.NotificationDto;
import com.outfit.ai.cloth_app.entity.Message;
import com.outfit.ai.cloth_app.service.MessageService;
import com.outfit.ai.cloth_app.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final NoticeService noticeService;

    public MessageController(MessageService messageService, NoticeService noticeService) {
        this.messageService = messageService;
        this.noticeService = noticeService;
    }

    // 쪽지 전송
    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        // 1️⃣ DB에 쪽지 저장
        Message savedMessage = messageService.sendMessage(message);

        // 2️⃣ 실시간 알림 전송
        NotificationDto notification = new NotificationDto(
                "MESSAGE",
                message.getContent(),
                message.getSender(),
                message.getReceiver()
        );
        noticeService.sendMessageNotice(message.getReceiver(), notification);

        return savedMessage;
    }

    // 받은 쪽지 조회
    @GetMapping("/inbox/{receiver}")
    public List<Message> getReceivedMessages(@PathVariable String receiver) {
        return messageService.getReceivedMessages(receiver);
    }

    // 보낸 쪽지 조회
    @GetMapping("/sent/{sender}")
    public List<Message> getSentMessages(@PathVariable String sender) {
        return messageService.getSentMessages(sender);
    }
}
