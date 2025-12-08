package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.tables.MessageTable;
import com.outfit.ai.cloth_app.tables.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageTable, Long> {
    List<MessageTable> findBySender(UserTable sender);
    List<MessageTable> findByReceiver(UserTable receiver);
}
