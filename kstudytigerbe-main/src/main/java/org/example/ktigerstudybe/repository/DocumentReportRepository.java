package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.DocumentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface DocumentReportRepository extends JpaRepository<DocumentReport, Long> {

    // Tìm tất cả báo cáo theo userId
    List<DocumentReport> findByUser_UserId(Long userId);

    // Tìm tất cả báo cáo theo listId (DocumentList ID)
    List<DocumentReport> findByDocumentList_ListId(Long listId);

    Page<DocumentReport> findByUser_UserId(Long userId, Pageable pageable); // NEW: return Page instead of List
    Page<DocumentReport> findByDocumentList_ListId(Long listId, Pageable pageable);
}
