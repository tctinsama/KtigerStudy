// src/main/java/org/example/ktigerstudybe/service/documentList/DocumentListService.java
package org.example.ktigerstudybe.service.documentList;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DocumentListService {

    /**
     * Tạo mới một bộ flashcard
     */
    DocumentListResponse createDocumentList(DocumentListRequest request);

    /**
     * Lấy về toàn bộ bộ flashcard
     */
    List<DocumentListResponse> getAllDocumentLists();

    /**
     * Lấy chi tiết bộ flashcard theo ID
     */
    DocumentListResponse getDocumentListById(Long id);



    List<DocumentListResponse> getDocumentListFavoritedByUserId(Long id);

    /**
     * Cập nhật bộ flashcard theo ID
     */
    DocumentListResponse updateDocumentList(Long id, DocumentListRequest request);

    /**
     * Xóa bộ flashcard theo ID
     */
    void deleteDocumentList(Long id);

    /**
     * Lấy các bộ flashcard của một user
     */
    List<DocumentListResponse> getDocumentListsByUserId(Long userId);

    /**
     * Lấy các bộ flashcard công khai (is_public = 0) không phân trang
     */
    List<DocumentListResponse> getPublicLists();

    /**
     * Lấy các bộ flashcard công khai (is_public = 0) có phân trang
     */
    Page<DocumentListResponse> getPublicLists(Pageable pageable);

    /**
     * Lấy các bộ flashcard theo type, và có thể filter isPublic (0 hoặc 1)
     */
    List<DocumentListResponse> getByTypeAndPublic(String type, int isPublic);

    void toggleVisibility(Long id);

    /**
     * Lấy các bộ flashcard của một user mà chưa gán vào lớp nào.
     */
    List<DocumentListResponse> getUnassignedByUserId(Long userId);


    /**
     * Nhóm các bộ flashcard theo type, mỗi nhóm tối đa 'limit' phần tử
     */
    Map<String, List<DocumentListResponse>> getGroupedByType(int limit);


    //admin
    Page<DocumentListResponse> listByUser(Long userId, Pageable pg);
    Page<DocumentListResponse> searchPublic(String keyword, Pageable pageable);
    Page<DocumentListResponse> searchPublicByTitleOrType(String keyword, Pageable pageable);
}
