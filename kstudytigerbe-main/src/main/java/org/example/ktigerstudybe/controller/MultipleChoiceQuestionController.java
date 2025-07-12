package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.MultipleChoiceQuestionRequest;
import org.example.ktigerstudybe.dto.resp.MultipleChoiceQuestionResponse;
import org.example.ktigerstudybe.service.multiplechoicequestion.MultipleChoiceQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcq")
public class MultipleChoiceQuestionController {

    @Autowired
    private MultipleChoiceQuestionService service;

    @GetMapping
    public List<MultipleChoiceQuestionResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultipleChoiceQuestionResponse> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exercise/{exerciseId}")
    public List<MultipleChoiceQuestionResponse> getByExercise(@PathVariable Long exerciseId) {
        return service.getByExerciseId(exerciseId);
    }

    @PostMapping
    public MultipleChoiceQuestionResponse create(@RequestBody MultipleChoiceQuestionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MultipleChoiceQuestionResponse> update(@PathVariable Long id, @RequestBody MultipleChoiceQuestionRequest request) {
        try {
            return ResponseEntity.ok(service.update(id, request));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    //admin
    // Phân trang và tìm kiếm theo lessonId
    @GetMapping("/lesson/{lessonId}/paged")
    public Page<MultipleChoiceQuestionResponse> getByLessonIdPaged(
            @PathVariable Long lessonId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return service.getByLessonIdPaged(lessonId, keyword, page, size);
    }
}