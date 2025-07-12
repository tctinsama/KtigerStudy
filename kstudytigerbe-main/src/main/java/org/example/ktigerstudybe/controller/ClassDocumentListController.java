package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.ClassDocumentListRequest;
import org.example.ktigerstudybe.dto.resp.ClassDocumentListResponse;
import org.example.ktigerstudybe.service.classDocumentList.ClassDocumentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-document-lists")
public class ClassDocumentListController {

    @Autowired
    private ClassDocumentListService service;

    @GetMapping
    public List<ClassDocumentListResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassDocumentListResponse> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ClassDocumentListResponse create(@RequestBody ClassDocumentListRequest request) {
        return service.create(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/class/{classId}")
    public List<ClassDocumentListResponse> getByClass(@PathVariable Long classId) {
        return service.getByClassId(classId);
    }

    @GetMapping("/list/{listId}")
    public List<ClassDocumentListResponse> getByList(@PathVariable Long listId) {
        return service.getByListId(listId);
    }
}
