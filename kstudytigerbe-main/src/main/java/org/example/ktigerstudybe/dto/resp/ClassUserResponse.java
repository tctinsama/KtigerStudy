package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassUserResponse {
    private Long classUserId;
    private Long classId;
    private String className;
    private Long userId;
    private String userFullName;
    private LocalDateTime joinedAt;
    private String email;
    private String avatarImage;
}
