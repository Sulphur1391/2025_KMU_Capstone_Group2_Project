package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.entity.Message;
import com.outfit.ai.cloth_app.entity.User;
import com.outfit.ai.cloth_app.repository.AppUserRepository; // 변경
import com.outfit.ai.cloth_app.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final AppUserRepository userRepository; // 변경

    public Message sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "보내는 사용자가 없습니다."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "받는 사용자가 없습니다."));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);

        return messageRepository.save(message);
    }

    public List<Message> getReceivedMessages(Long receiverId) {
        return messageRepository.findByReceiver_Id(receiverId);
    }

    public List<Message> getSentMessages(Long senderId) {
        return messageRepository.findBySender_Id(senderId);
    }

    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메시지가 존재하지 않습니다.");
        }
        messageRepository.deleteById(id);
    }
}
