package org.example.ktigerstudybe.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String avatarImage;
    private LocalDateTime joinDate;
    private String currentLevel;
    private String currentLesson;
    private Integer completedLessons;
    private Integer totalLessons;
    private Double progressPercentage;
    private LocalDateTime lastAccessed;
    private String status; // "active", "inactive", "completed"
}
