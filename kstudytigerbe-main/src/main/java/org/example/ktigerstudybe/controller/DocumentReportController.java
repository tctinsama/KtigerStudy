package org.example.ktigerstudybe.controller;

import lombok.RequiredArgsConstructor;
import org.example.ktigerstudybe.dto.req.DocumentReportRequest;
import org.example.ktigerstudybe.dto.resp.DocumentReportResponse;
import org.example.ktigerstudybe.service.documentReport.DocumentReportService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-reports")
@RequiredArgsConstructor
public class DocumentReportController {
    private final DocumentReportService documentReportService;

    // Non-paginated endpoints
    @GetMapping
    public List<DocumentReportResponse> getAllReports() {
        return documentReportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentReportResponse> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(documentReportService.getReportById(id));
    }

    @PostMapping
    public ResponseEntity<DocumentReportResponse> createReport(@RequestBody DocumentReportRequest request) {
        return ResponseEntity.ok(documentReportService.createReport(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        documentReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<DocumentReportResponse> getReportsByUserId(@PathVariable Long userId) {
        return documentReportService.getReportsByUserId(userId);
    }

    @GetMapping("/document/{listId}")
    public List<DocumentReportResponse> getReportsByListId(@PathVariable Long listId) {
        return documentReportService.getReportsByListId(listId);
    }

    // Paginated endpoints (avoid collision by adding "/paged")
    @GetMapping("/paged")
    public ResponseEntity<Page<DocumentReportResponse>> getPagedReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentReportService.getAllReports(page, size));
    }

    @GetMapping("/user/{userId}/paged")
    public ResponseEntity<Page<DocumentReportResponse>> getPagedReportsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentReportService.getReportsByUserId(userId, page, size));
    }

    @GetMapping("/document/{listId}/paged")
    public ResponseEntity<Page<DocumentReportResponse>> getPagedReportsByDocument(
            @PathVariable Long listId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentReportService.getReportsByListId(listId, page, size));
    }
}
