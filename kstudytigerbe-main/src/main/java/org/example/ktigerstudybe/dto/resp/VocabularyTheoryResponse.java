package org.example.ktigerstudybe.dto.resp;

import lombok.Data;
import org.example.ktigerstudybe.model.Lesson;

@Data
public class VocabularyTheoryResponse {
    private long vocabId;
    private long lessonId;
    private String word;
    private String meaning;
    private String example;
    private Long levelId;
    private String levelName;
    private String image;
}
