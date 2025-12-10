package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.entity.tables.MessageTable;
import com.outfit.ai.cloth_app.entity.tables.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MessageTableRepository extends JpaRepository<MessageTable, UUID> {
    // FIXED: 메시지 조회를 receiver user의 id 기준으로 하도록 메서드명 변경
    List<MessageTable> findByReceiverId(UUID receiverId);
    List<MessageTable> findBySenderId(UUID senderId);
    List<MessageTable> findByReceiver(UserTable receiver);
    List<MessageTable> findBySender(UserTable sender);
}
