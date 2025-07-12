package org.example.ktigerstudybe.service.exercise;

import org.example.ktigerstudybe.dto.req.ExerciseRequest;
import org.example.ktigerstudybe.dto.resp.ExerciseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExerciseService {
    List<ExerciseResponse> getAllExercises();
    ExerciseResponse getExerciseById(Long id);
    ExerciseResponse createExercise(ExerciseRequest request);
    ExerciseResponse updateExercise(Long id, ExerciseRequest request);
    void deleteExercise(Long id);
    List<ExerciseResponse> getExercisesByLessonId(Long lessonId);

    //ad
    Page<ExerciseResponse> getExercisesByLessonIdPaged(Long lessonId, String title, int page, int size);

}