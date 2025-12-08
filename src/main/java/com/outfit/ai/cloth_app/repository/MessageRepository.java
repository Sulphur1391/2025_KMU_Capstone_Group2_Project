package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // FIXED: 메시지 조회를 receiver user의 id 기준으로 하도록 메서드명 변경
    List<Message> findByReceiver_Id(Long receiverId);
    List<Message> findBySender_Id(Long senderId);
}
