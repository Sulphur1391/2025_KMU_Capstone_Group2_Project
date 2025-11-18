package com.outfit.ai.cloth_app.service;

import com.outfit.ai.cloth_app.dto.MessageDto;
import com.outfit.ai.cloth_app.repository.MessageRepository;
import com.outfit.ai.cloth_app.repository.UserRepository;
import com.outfit.ai.cloth_app.tables.MessageTable;
import com.outfit.ai.cloth_app.tables.UserTable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

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

    public List<MessageDto> getInbox(UUID receiverId) {
        UserTable receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        List<MessageTable> messages = messageRepository.findByReceiver(receiver);

        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MessageDto> getSent(UUID senderId) {
        UserTable sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        List<MessageTable> messages = messageRepository.findBySender(sender);

        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
