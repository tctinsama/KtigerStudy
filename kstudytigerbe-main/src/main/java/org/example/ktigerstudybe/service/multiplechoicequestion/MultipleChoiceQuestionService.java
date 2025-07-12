package org.example.ktigerstudybe.service.multiplechoicequestion;


import org.example.ktigerstudybe.dto.req.MultipleChoiceQuestionRequest;
import org.example.ktigerstudybe.dto.resp.MultipleChoiceQuestionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MultipleChoiceQuestionService {
    List<MultipleChoiceQuestionResponse> getAll();
    MultipleChoiceQuestionResponse getById(Long id);
    MultipleChoiceQuestionResponse create(MultipleChoiceQuestionRequest request);
    MultipleChoiceQuestionResponse update(Long id, MultipleChoiceQuestionRequest request);
    void delete(Long id);
    List<MultipleChoiceQuestionResponse> getByExerciseId(Long exerciseId);

    //admin
    Page<MultipleChoiceQuestionResponse> getByLessonIdPaged(Long lessonId, String keyword, int page, int size);}