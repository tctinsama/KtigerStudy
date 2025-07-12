package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.GrammarTheoryRequest;
import org.example.ktigerstudybe.dto.resp.GrammarTheoryResponse;
import org.example.ktigerstudybe.service.grammarTheory.GrammarTheoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/grammar-theories")
public class GrammarTheoryController {

    @Autowired
    private GrammarTheoryService grammarTheoryService;

    // Lấy danh sách tất cả grammar theory
    @GetMapping
    public List<GrammarTheoryResponse> getAllGrammarTheories() {
        return grammarTheoryService.getAllGrammarTheories();
    }

    // Lấy chi tiết grammar theory theo id
    @GetMapping("/{id}")
    public ResponseEntity<GrammarTheoryResponse> getGrammarTheoryById(@PathVariable Long id) {
        try {
            GrammarTheoryResponse resp = grammarTheoryService.getGrammarTheoryById(id);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo mới grammar theory
    @PostMapping
    public GrammarTheoryResponse createGrammarTheory(@RequestBody GrammarTheoryRequest request) {
        return grammarTheoryService.createGrammarTheory(request);
    }

    // Sửa grammar theory
    @PutMapping("/{id}")
    public ResponseEntity<GrammarTheoryResponse> updateGrammarTheory(
            @PathVariable Long id,
            @RequestBody GrammarTheoryRequest request) {
        try {
            GrammarTheoryResponse updated = grammarTheoryService.updateGrammarTheory(id, request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa grammar theory
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrammarTheory(@PathVariable Long id) {
        grammarTheoryService.deleteGrammarTheory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lesson/{lessonId}")
    public List<GrammarTheoryResponse> getGrammarByLesson(@PathVariable Long lessonId) {
        return grammarTheoryService.getGrammarByLessonId(lessonId);
    }

    @GetMapping("/level/{levelId}")
    public List<GrammarTheoryResponse> getGrammarByLevel(@PathVariable Long levelId) {
        return grammarTheoryService.getGrammarByLevelId(levelId);
    }


    //admin
    // Sử dụng service với method mới
    @GetMapping("/lessons/{lessonId}/grammar/paged")
    public Page<GrammarTheoryResponse> getGrammarPaged(
            @PathVariable Long lessonId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return grammarTheoryService.getPagedGrammarByLesson(lessonId, searchTerm, page, size);
    }
}
