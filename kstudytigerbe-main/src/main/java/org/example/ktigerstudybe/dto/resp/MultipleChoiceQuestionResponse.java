package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class MultipleChoiceQuestionResponse {
    private Long questionId;
    private Long exerciseId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String linkMedia;
}