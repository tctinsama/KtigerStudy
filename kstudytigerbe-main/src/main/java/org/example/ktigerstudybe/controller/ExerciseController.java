package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.ExerciseRequest;
import org.example.ktigerstudybe.dto.resp.ExerciseResponse;
import org.example.ktigerstudybe.service.exercise.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping
    public List<ExerciseResponse> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@PathVariable Long id) {
        try {
            ExerciseResponse resp = exerciseService.getExerciseById(id);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lesson/{lessonId}")
    public List<ExerciseResponse> getExercisesByLesson(@PathVariable Long lessonId) {
        return exerciseService.getExercisesByLessonId(lessonId);
    }

    @PostMapping
    public ExerciseResponse createExercise(@RequestBody ExerciseRequest request) {
        return exerciseService.createExercise(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @PathVariable Long id,
            @RequestBody ExerciseRequest request) {
        try {
            ExerciseResponse updated = exerciseService.updateExercise(id, request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    //ad
    @GetMapping("/lesson/{lessonId}/paged")
    public Page<ExerciseResponse> getExercisesByLessonPaged(
            @PathVariable Long lessonId,
            @RequestParam(defaultValue = "") String title,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return exerciseService.getExercisesByLessonIdPaged(lessonId, title, page, size);
    }
}
