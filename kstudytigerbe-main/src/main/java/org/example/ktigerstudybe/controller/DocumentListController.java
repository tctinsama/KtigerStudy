// src/main/java/org/example/ktigerstudybe/controller/DocumentListController.java
package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.service.documentList.DocumentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/document-lists")
public class DocumentListController {

    private final DocumentListService service;

    @Autowired
    public DocumentListController(DocumentListService service) {
        this.service = service;
    }

    /**
     * 1) Lấy tất cả các bộ public (is_public = 0)
     */
    @GetMapping("/public")
    public ResponseEntity<Page<DocumentListResponse>> getPublicLists(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size
    ) {
        // Tạo Pageable với sort giảm dần theo createdAt (thay field khác nếu cần)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DocumentListResponse> result = service.getPublicLists(pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * 2) Lấy danh sách các loại (distinct types)
     */
//    @GetMapping("/distinct-types")
//    public List<String> getDistinctTypes() {
//        return service.getDistinctTypes();
//    }

    /**
     * 3) Lấy grouped theo type, mỗi type tối đa 4 items
     */
    @GetMapping("/grouped")
    public Map<String, List<DocumentListResponse>> getGroupedByType() {
        return service.getGroupedByType(4);
    }

    /**
     * 4) Lấy theo type, filter isPublic (mặc định 0 = public)
     */
    @GetMapping("/type/{type}")
    public List<DocumentListResponse> getByTypeAndPublic(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int isPublic
    ) {
        return service.getByTypeAndPublic(type, isPublic);
    }

    /**
     * 5) Lấy theo userId
     */
    @GetMapping("/user/{userId}")
    public List<DocumentListResponse> getByUser(@PathVariable Long userId) {
        return service.getDocumentListsByUserId(userId);
    }

    /**
     * 6) Lấy chi tiết theo id (chỉ khớp số)
     */
    @GetMapping("/{id:\\d+}")
    public DocumentListResponse getById(@PathVariable Long id) {
        return service.getDocumentListById(id);
    }

    /**
     * 7) Tạo mới
     */
    @PostMapping
    public DocumentListResponse create(@RequestBody DocumentListRequest request) {
        return service.createDocumentList(request);
    }

    /**
     * 8) Cập nhật theo id (chỉ khớp số)
     */
    @PutMapping("/{id:\\d+}")
    public DocumentListResponse update(
            @PathVariable Long id,
            @RequestBody DocumentListRequest request
    ) {
        return service.updateDocumentList(id, request);
    }

    /**
     * 9) Xóa theo id (chỉ khớp số)
     */
    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable Long id) {
        service.deleteDocumentList(id);
    }

    //admin
    // Tìm kiếm tất cả tài liệu theo title hoặc tên tác giả
    @GetMapping("/public/paged")
    public Page<DocumentListResponse> getPublicPaged(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return service.searchPublic(keyword, pageable);
    }

    // Lấy tất cả tài liệu của user
    @GetMapping("/user/{userId}/paged")
    public Page<DocumentListResponse> getByUserPaged(
            @PathVariable Long userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return service.listByUser(userId, pageable);
    }

    /**
     * 10) Tìm kiếm theo title hoặc type, chỉ cần 1 ô input
     *    Ví dụ: GET /api/document-lists/search?keyword=java&page=0&size=10
     */
//    @GetMapping("/search")
//    public ResponseEntity<Page<DocumentListResponse>> searchPublicByTitleOrType(
//            @RequestParam("keyword") String keyword,
//            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
//            Pageable pageable
//    ) {
//        Page<DocumentListResponse> result = service.searchPublicByTitleOrType(keyword, pageable);
//        return ResponseEntity.ok(result);
//    }

    /**
     * 11) Đảo ngược trạng thái công khai của DocumentList:
     *     nếu isPublic = 0 → thành 1, ngược lại 1 → thành 0
     */
    @PatchMapping("/{id:\\d+}/visibility")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleVisibility(@PathVariable Long id) {
        service.toggleVisibility(id);
    }


    /**
     * 12) Lấy tất cả DocumentList mà user này đã đánh dấu favorite
     */
    @GetMapping("/favorited/{userId}")
    public List<DocumentListResponse> getFavoritedByUser(@PathVariable("userId") Long userId) {
        return service.getDocumentListFavoritedByUserId(userId);
    }

    /**
     * 13) Lấy các bộ flashcard của user mà chưa gán vào bất cứ lớp nào
     */
    @GetMapping("/user/{userId}/unassigned")
    public List<DocumentListResponse> getUnassignedByUser(@PathVariable Long userId) {
        return service.getUnassignedByUserId(userId);
    }
}
