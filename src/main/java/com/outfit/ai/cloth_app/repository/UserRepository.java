package com.outfit.ai.cloth_app.repository;

<<<<<<< HEAD
import com.outfit.ai.cloth_app.tables.UserTable;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserTable, UUID> {
    boolean existsByEmail(String email);
    Optional<UserTable> findByEmail(String email);
    Optional<UserTable> findByUsername(String username);
}
=======
import com.outfit.ai.cloth_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
>>>>>>> origin/main
