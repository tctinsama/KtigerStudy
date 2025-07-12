package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class UserExerciseResultRequest {
    private Long userId;
    private Long exerciseId;
    private Integer score;
    private String dateComplete;
}