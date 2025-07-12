package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.DocumentItemRequest;
import org.example.ktigerstudybe.dto.resp.DocumentItemResponse;
import org.example.ktigerstudybe.service.documentItem.DocumentItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-items")
public class DocumentItemController {

    @Autowired
    private DocumentItemService documentItemService;

    // Lấy tất cả các mục từ vựng
    @GetMapping
    public List<DocumentItemResponse> getAllDocumentItems() {
        return documentItemService.getAllDocumentItems();
    }

    // Lấy mục từ vựng theo ID
    @GetMapping("/{wordId}")
    public ResponseEntity<DocumentItemResponse> getDocumentItemById(@PathVariable Long wordId) {
        try {
            DocumentItemResponse response = documentItemService.getDocumentItemById(wordId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách mục từ vựng theo ListID
    @GetMapping("/list/{listId}")
    public List<DocumentItemResponse> getDocumentItemsByListId(@PathVariable Long listId) {
        return documentItemService.getDocumentItemsByListId(listId);
    }

    // Tạo mới mục từ vựng
    @PostMapping
    public DocumentItemResponse createDocumentItem(@RequestBody DocumentItemRequest request) {
        return documentItemService.createDocumentItem(request);
    }

    // Cập nhật mục từ vựng
    @PutMapping("/{wordId}")
    public ResponseEntity<DocumentItemResponse> updateDocumentItem(
            @PathVariable Long wordId,
            @RequestBody DocumentItemRequest request) {
        try {
            DocumentItemResponse updated = documentItemService.updateDocumentItem(wordId, request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa mục từ vựng theo WordID
    @DeleteMapping("/{wordId}")
    public ResponseEntity<Void> deleteDocumentItem(@PathVariable Long wordId) {
        documentItemService.deleteDocumentItem(wordId);
        return ResponseEntity.noContent().build();
    }

    // Xóa toàn bộ mục từ vựng theo ListID
    @DeleteMapping("/list/{listId}")
    public ResponseEntity<Void> deleteDocumentItemsByListId(@PathVariable Long listId) {
        documentItemService.deleteDocumentItemsByListId(listId);
        return ResponseEntity.noContent().build();
    }


    //admin
    @GetMapping("/list/{listId}/paged")
    public Page<DocumentItemResponse> getByListPaged(
            @PathVariable Long listId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 1) Pageable pageable
    ) {
        return documentItemService.getDocumentItemsPaged(listId, keyword, pageable);
    }

}
