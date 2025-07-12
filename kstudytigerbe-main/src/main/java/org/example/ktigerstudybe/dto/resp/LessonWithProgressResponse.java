package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class LessonWithProgressResponse {
    private Long lessonId;
    private String lessonName;
    private String lessonDescription;
    private boolean lessonCompleted;
    private boolean locked;
}
