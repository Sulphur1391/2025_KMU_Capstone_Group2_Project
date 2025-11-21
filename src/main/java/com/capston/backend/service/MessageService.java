package com.capston.backend.service;

import com.capston.backend.entity.Message;
import com.capston.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 쪽지 전송
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    // 받은 쪽지 조회
    public List<Message> getReceivedMessages(String receiver) {
        return messageRepository.findByReceiver(receiver);
    }

    // 보낸 쪽지 조회
    public List<Message> getSentMessages(String sender) {
        return messageRepository.findBySender(sender);
    }

    // 쪽지 삭제
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
