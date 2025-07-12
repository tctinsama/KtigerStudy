package org.example.ktigerstudybe.dto.req;

import lombok.Data;
import org.example.ktigerstudybe.model.Lesson;

@Data
public class VocabularyTheoryRequest {
    private Long lessonId;
    private String word;
    private String meaning;
    private String example;
    private String image;
}
