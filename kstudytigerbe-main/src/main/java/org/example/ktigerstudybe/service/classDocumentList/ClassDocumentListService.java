package org.example.ktigerstudybe.service.classDocumentList;

import org.example.ktigerstudybe.dto.req.ClassDocumentListRequest;
import org.example.ktigerstudybe.dto.resp.ClassDocumentListResponse;

import java.util.List;

public interface ClassDocumentListService {
    List<ClassDocumentListResponse> getAll();
    ClassDocumentListResponse getById(Long id);
    ClassDocumentListResponse create(ClassDocumentListRequest request);
    void delete(Long id);
    List<ClassDocumentListResponse> getByClassId(Long classId);
    List<ClassDocumentListResponse> getByListId(Long listId);
}
