package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class SentenceRewritingQuestionRequest {
    private Long exerciseId;
    private String originalSentence;
    private String rewrittenSentence;
    private String linkMedia;
}