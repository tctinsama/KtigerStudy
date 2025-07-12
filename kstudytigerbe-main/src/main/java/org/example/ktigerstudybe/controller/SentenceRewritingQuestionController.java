package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.SentenceRewritingQuestionRequest;
import org.example.ktigerstudybe.dto.resp.SentenceRewritingQuestionResponse;
import org.example.ktigerstudybe.service.sentencerewritingquestion.SentenceRewritingQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sentence-rewriting")
public class SentenceRewritingQuestionController {

    @Autowired
    private SentenceRewritingQuestionService service;

    @GetMapping
    public List<SentenceRewritingQuestionResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SentenceRewritingQuestionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public SentenceRewritingQuestionResponse create(@RequestBody SentenceRewritingQuestionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public SentenceRewritingQuestionResponse update(@PathVariable Long id, @RequestBody SentenceRewritingQuestionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/exercise/{exerciseId}")
    public List<SentenceRewritingQuestionResponse> getByExercise(@PathVariable Long exerciseId) {
        return service.getByExerciseId(exerciseId);
    }

    //ad
    // Phân trang và tìm kiếm theo lessonId cho admin
    @GetMapping("/lesson/{lessonId}/paged")
    public Page<SentenceRewritingQuestionResponse> getByLessonIdPaged(
            @PathVariable Long lessonId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return service.getByLessonIdPaged(lessonId, keyword, page, size);
    }
}
