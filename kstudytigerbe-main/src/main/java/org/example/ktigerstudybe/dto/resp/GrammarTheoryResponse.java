package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class GrammarTheoryResponse {
    private Long grammarId;
    private Long lessonId;
    private String grammarTitle;
    private String grammarContent;
    private String grammarExample;
    private Long levelId;
    private String levelName;
}