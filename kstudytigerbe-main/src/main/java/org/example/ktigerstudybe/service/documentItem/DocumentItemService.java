package org.example.ktigerstudybe.service.documentItem;

import org.example.ktigerstudybe.dto.req.DocumentItemRequest;
import org.example.ktigerstudybe.dto.resp.DocumentItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentItemService {

    // Lấy tất cả các mục từ vựng
    List<DocumentItemResponse> getAllDocumentItems();

    // Lấy một mục từ vựng theo ID
    DocumentItemResponse getDocumentItemById(Long wordId);

    // Lấy danh sách mục từ vựng theo ListID
    List<DocumentItemResponse> getDocumentItemsByListId(Long listId);

    // Tạo mới mục từ vựng
    DocumentItemResponse createDocumentItem(DocumentItemRequest request);

    // Cập nhật mục từ vựng
    DocumentItemResponse updateDocumentItem(Long wordId, DocumentItemRequest request);

    // Xóa mục từ vựng theo WordID
    void deleteDocumentItem(Long wordId);

    // Xóa toàn bộ mục từ vựng theo ListID
    void deleteDocumentItemsByListId(Long listId);

    //admin
    Page<DocumentItemResponse> getDocumentItemsPaged(
            Long listId, String keyword, Pageable pageable
    );


}
