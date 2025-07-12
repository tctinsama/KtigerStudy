package org.example.ktigerstudybe.repository;


import org.example.ktigerstudybe.model.PasswordResetToken;
import org.example.ktigerstudybe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}