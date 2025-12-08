package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.dto.MessageDto;
<<<<<<< HEAD
import com.outfit.ai.cloth_app.repository.MessageRepository;
import com.outfit.ai.cloth_app.repository.UserRepository;
import com.outfit.ai.cloth_app.tables.MessageTable;
import com.outfit.ai.cloth_app.tables.UserTable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// 쪽지 서비스
@Service
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // 쪽지 보내기
    public MessageDto sendMessage(UUID senderId, MessageDto messageDto) {
        UserTable sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found."));
        UserTable receiver = userRepository.findByUsername(messageDto.getReceiver())
                .orElseThrow(() -> new RuntimeException("Receiver not found."));

        MessageTable message = new MessageTable(
                sender,
                receiver,
                messageDto.getContent()
        );

        MessageTable savedMessage = messageRepository.save(message);

        return MessageDto.fromEntity(savedMessage);
    }

    // 쪽지함 불러오기
    public List<MessageDto> getInbox(UUID receiverId) {
        UserTable receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        List<MessageTable> messages = messageRepository.findByReceiver(receiver);

        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 받은 쪽지 불러오기
    public List<MessageDto> getSent(UUID senderId) {
        UserTable sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        List<MessageTable> messages = messageRepository.findBySender(sender);

        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
=======
import com.outfit.ai.cloth_app.entity.Message;
import com.outfit.ai.cloth_app.entity.User;
import com.outfit.ai.cloth_app.repository.MessageRepository;
import com.outfit.ai.cloth_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MessageService
 * - Message 엔티티를 그대로 Controller에 반환하지 않도록 권장 (LAZY proxy, 순환참조 문제)
 * - sendMessage에서 알림(NoticeService) 호출은 Controller에서 하거나 이 서비스에서 수행 가능.
 *   (현재는 Controller에서 알림을 호출하므로 여기서는 저장만 수행)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

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
>>>>>>> origin/main
    }
}
