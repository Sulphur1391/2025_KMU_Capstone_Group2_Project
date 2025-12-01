package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiver(String receiver);  // 받은 쪽지 조회
    List<Message> findBySender(String sender);      // 보낸 쪽지 조회
}
