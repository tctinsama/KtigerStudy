package org.example.ktigerstudybe.service.grammarTheory;

import org.example.ktigerstudybe.dto.req.GrammarTheoryRequest;
import org.example.ktigerstudybe.dto.resp.GrammarTheoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GrammarTheoryService {
    List<GrammarTheoryResponse> getAllGrammarTheories();
    GrammarTheoryResponse getGrammarTheoryById(Long id);
    GrammarTheoryResponse createGrammarTheory(GrammarTheoryRequest request);
    GrammarTheoryResponse updateGrammarTheory(Long id, GrammarTheoryRequest request);
    void deleteGrammarTheory(Long id);
    List<GrammarTheoryResponse> getGrammarByLessonId(Long lessonId);
    List<GrammarTheoryResponse> getGrammarByLevelId(Long levelId);

    //admin
    Page<GrammarTheoryResponse> getPagedGrammarByLesson(Long lessonId, String searchTerm, int page, int size);
}