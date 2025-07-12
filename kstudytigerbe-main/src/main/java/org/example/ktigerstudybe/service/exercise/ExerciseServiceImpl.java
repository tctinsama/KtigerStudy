package org.example.ktigerstudybe.service.exercise;

import org.example.ktigerstudybe.dto.req.ExerciseRequest;
import org.example.ktigerstudybe.dto.resp.ExerciseResponse;
import org.example.ktigerstudybe.model.Exercise;
import org.example.ktigerstudybe.model.Lesson;
import org.example.ktigerstudybe.repository.ExerciseRepository;
import org.example.ktigerstudybe.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private ExerciseResponse toResponse(Exercise entity) {
        ExerciseResponse resp = new ExerciseResponse();
        resp.setExerciseId(entity.getExerciseId());
        resp.setLessonId(entity.getLesson().getLessonId());
        resp.setExerciseTitle(entity.getExerciseTitle());
        resp.setExerciseType(entity.getExerciseType());
        resp.setExerciseDescription(entity.getExerciseDescription());
        return resp;
    }

    private Exercise toEntity(ExerciseRequest req) {
        Exercise entity = new Exercise();
        Lesson lesson = lessonRepository.findById(req.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + req.getLessonId()));
        entity.setLesson(lesson);
        entity.setExerciseTitle(req.getExerciseTitle());
        entity.setExerciseType(req.getExerciseType());
        entity.setExerciseDescription(req.getExerciseDescription());
        return entity;
    }

    @Override
    public List<ExerciseResponse> getAllExercises() {
        return exerciseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseResponse getExerciseById(Long id) {
        Exercise entity = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + id));
        return toResponse(entity);
    }

    @Override
    public ExerciseResponse createExercise(ExerciseRequest request) {
        Exercise entity = toEntity(request);
        entity = exerciseRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public ExerciseResponse updateExercise(Long id, ExerciseRequest request) {
        Exercise entity = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + id));
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + request.getLessonId()));
        entity.setLesson(lesson);
        entity.setExerciseTitle(request.getExerciseTitle());
        entity.setExerciseType(request.getExerciseType());
        entity.setExerciseDescription(request.getExerciseDescription());
        entity = exerciseRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }

    @Override
    public List<ExerciseResponse> getExercisesByLessonId(Long lessonId) {
        return exerciseRepository.findByLesson_LessonId(lessonId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    //ad
    @Override
    public Page<ExerciseResponse> getExercisesByLessonIdPaged(Long lessonId, String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Exercise> pageResult = exerciseRepository.findByLesson_LessonIdAndExerciseTitleContainingIgnoreCase(
                lessonId, title == null ? "" : title, pageable
        );
        return pageResult.map(this::toResponse);
    }
}
