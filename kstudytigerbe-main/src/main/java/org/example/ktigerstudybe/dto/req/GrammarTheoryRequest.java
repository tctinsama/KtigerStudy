package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class GrammarTheoryRequest {
    private Long lessonId;            // ID của bài học liên quan
    private String grammarTitle;
    private String grammarContent;
    private String grammarExample;
}
