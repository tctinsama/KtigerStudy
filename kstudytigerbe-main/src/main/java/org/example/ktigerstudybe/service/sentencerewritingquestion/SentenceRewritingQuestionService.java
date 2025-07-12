package org.example.ktigerstudybe.service.sentencerewritingquestion;

import org.example.ktigerstudybe.dto.req.SentenceRewritingQuestionRequest;
import org.example.ktigerstudybe.dto.resp.SentenceRewritingQuestionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SentenceRewritingQuestionService {
    List<SentenceRewritingQuestionResponse> getAll();
    SentenceRewritingQuestionResponse getById(Long id);
    SentenceRewritingQuestionResponse create(SentenceRewritingQuestionRequest request);
    SentenceRewritingQuestionResponse update(Long id, SentenceRewritingQuestionRequest request);
    void delete(Long id);
    List<SentenceRewritingQuestionResponse> getByExerciseId(Long exerciseId);

    //ad
    Page<SentenceRewritingQuestionResponse> getByLessonIdPaged(Long lessonId, String keyword, int page, int size);
}