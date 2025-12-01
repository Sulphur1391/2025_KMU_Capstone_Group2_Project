package com.outfit.ai.cloth_app.repository;

import com.outfit.ai.cloth_app.tables.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCalendarRepository extends JpaRepository<UserCalendar, UUID> {
    boolean existsByGoogleEventId(String googleEventId);
}
