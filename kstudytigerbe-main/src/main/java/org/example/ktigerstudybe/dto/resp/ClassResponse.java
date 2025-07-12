package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassResponse {
    private Long classId;
    private String className;
    private String description;
    private Long userId;
    private String userFullName;
    private LocalDateTime createdAt;
    private String password;

}
