package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.entity.Message;
import com.outfit.ai.cloth_app.entity.User;
import com.outfit.ai.cloth_app.repository.MessageRepository;
import com.outfit.ai.cloth_app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository; // FIXED: User 확인용

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // 쪽지 전송
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

    // 받은 쪽지 조회
    public List<Message> getReceivedMessages(Long receiverId) {
        // FIXED: Receiver id로 조회
        return messageRepository.findByReceiver_Id(receiverId);
    }

    // 보낸 쪽지 조회
    public List<Message> getSentMessages(Long senderId) {
        return messageRepository.findBySender_Id(senderId);
    }

    // 쪽지 삭제 (권한 체크는 컨트롤러나 추가 로직에서 처리 권장)
    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메시지가 존재하지 않습니다.");
        }
        messageRepository.deleteById(id);
    }
}
