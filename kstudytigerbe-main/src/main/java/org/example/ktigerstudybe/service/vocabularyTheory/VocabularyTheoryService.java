package org.example.ktigerstudybe.service.vocabularyTheory;

import org.example.ktigerstudybe.dto.req.VocabularyTheoryRequest;
import org.example.ktigerstudybe.dto.resp.VocabularyTheoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VocabularyTheoryService {
    List<VocabularyTheoryResponse> getAllVocabularyTheories();
    VocabularyTheoryResponse getVocabularyTheoryById(Long id);
    VocabularyTheoryResponse createVocabularyTheory(VocabularyTheoryRequest request);
    VocabularyTheoryResponse updateVocabularyTheory(Long id, VocabularyTheoryRequest request);
    void deleteVocabularyTheory(Long id);
    List<VocabularyTheoryResponse> getVocabulariesByLessonId(Long lessonId);
    List<VocabularyTheoryResponse> getVocabulariesByLevelId(Long levelId);

    //admin
    Page<VocabularyTheoryResponse> getPagedVocabByLesson(Long lessonId, String searchTerm, int page, int size);

}
