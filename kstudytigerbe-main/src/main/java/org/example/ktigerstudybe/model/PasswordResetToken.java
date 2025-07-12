package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // hoặc code OTP

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "UserID")
    private User user;

    private LocalDateTime expiryDate; // Thời hạn (ví dụ 15 phút)
}
