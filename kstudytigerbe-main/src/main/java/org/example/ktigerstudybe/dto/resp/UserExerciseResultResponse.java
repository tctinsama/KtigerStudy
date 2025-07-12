package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class UserExerciseResultResponse {
    private Long resultId;
    private Long userId;
    private Long exerciseId;
    private Integer score;
    private String dateComplete;
}
