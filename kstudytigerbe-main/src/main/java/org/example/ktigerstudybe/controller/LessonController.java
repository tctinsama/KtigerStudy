package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.LessonRequest;
import org.example.ktigerstudybe.dto.resp.LessonResponse;
import org.example.ktigerstudybe.dto.resp.LessonWithProgressResponse;
import org.example.ktigerstudybe.service.lesson.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

//    @GetMapping
//    public List<LessonResponse> getAllLessons() {
//        return lessonService.getAllLessons();
//    }

    // Lấy tất cả bài học hoặc theo Level
    @GetMapping
    public List<LessonResponse> getLessons(@RequestParam(required = false) Long levelId) {
        if (levelId != null) {
            return lessonService.getLessonsByLevelId(levelId);
        }
        return lessonService.getAllLessons();
    }


//    @GetMapping
//    public List<LessonResponse> getAllLessons() {
//        return lessonService.getAllLessons();
//    }

    @GetMapping("/{id}")
    public LessonResponse getLessonById(@PathVariable Long id) {
        return lessonService.getLessonById(id);
    }


    @PostMapping
    public LessonResponse createLesson(@RequestBody LessonRequest lessonRequest) {
        return lessonService.createLesson(lessonRequest);
    }

    @PutMapping("/{id}")
    public LessonResponse updateLesson(@PathVariable Long id, @RequestBody LessonRequest lessonRequest) {
        return lessonService.updateLesson(id, lessonRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/progress")
    public List<LessonWithProgressResponse> getLessonsWithProgress(
            @RequestParam Long levelId,
            @RequestParam Long userId
    ) {
        return lessonService.getLessonsWithProgress(levelId, userId);
    }
    @PostMapping("/complete")
    public Map<String, Object> completeLesson(
            @RequestParam Long userId,
            @RequestParam Long lessonId,
            @RequestParam Integer score
    ) {
        return lessonService.completeLesson(userId, lessonId, score);
    }

    //admin
    // GET paginated + search (Page)
    @GetMapping("/paged")
    public ResponseEntity<Page<LessonResponse>> getLessonsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long levelId,
            @RequestParam(required = false) String keyword
    ) {
        Page<LessonResponse> result = lessonService.getLessons(page, size, levelId, keyword);
        return ResponseEntity.ok(result);
    }


}
