package com.capston.backend.controller;

import com.capston.backend.dto.MessageDto;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final Map<Long, MessageDto> messageStore = new HashMap<>();
    private long messageIdSeq = 1;

    /** 쪽지 보내기 */
    @PostMapping
    public MessageDto sendMessage(@RequestBody MessageDto message) {
        message.setId(messageIdSeq++);
        messageStore.put(message.getId(), message);
        return message;
    }

    /** 특정 사용자에게 온 쪽지 목록 조회 */
    @GetMapping("/inbox/{user}")
    public List<MessageDto> getInbox(@PathVariable String user) {
        List<MessageDto> inbox = new ArrayList<>();
        for (MessageDto msg : messageStore.values()) {
            if (msg.getReceiver().equals(user)) inbox.add(msg);
        }
        return inbox;
    }

    /** 특정 사용자가 보낸 쪽지 목록 조회 */
    @GetMapping("/sent/{user}")
    public List<MessageDto> getSent(@PathVariable String user) {
        List<MessageDto> sent = new ArrayList<>();
        for (MessageDto msg : messageStore.values()) {
            if (msg.getSender().equals(user)) sent.add(msg);
        }
        return sent;
    }
}
