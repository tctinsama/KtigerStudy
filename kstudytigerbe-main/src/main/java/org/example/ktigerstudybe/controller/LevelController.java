package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.LevelRequest;
import org.example.ktigerstudybe.dto.resp.LevelResponse;
import org.example.ktigerstudybe.service.level.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @GetMapping
    public List<LevelResponse> getAllLevels() {
        return levelService.getAllLevels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevelResponse> getLevelById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(levelService.getLevelById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public LevelResponse createLevel(@RequestBody LevelRequest request) {
        return levelService.createLevel(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LevelResponse> updateLevel(@PathVariable Long id, @RequestBody LevelRequest request) {
        try {
            return ResponseEntity.ok(levelService.updateLevel(id, request));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        levelService.deleteLevel(id);
        return ResponseEntity.noContent().build();
    }
}