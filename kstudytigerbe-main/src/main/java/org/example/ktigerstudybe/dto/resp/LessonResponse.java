package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class LessonResponse {
    private Long lessonId;
    private String lessonName;
    private String lessonDescription;
}
