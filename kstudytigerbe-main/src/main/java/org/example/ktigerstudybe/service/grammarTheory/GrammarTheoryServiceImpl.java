package org.example.ktigerstudybe.service.grammarTheory;

import org.example.ktigerstudybe.dto.req.GrammarTheoryRequest;
import org.example.ktigerstudybe.dto.resp.GrammarTheoryResponse;
import org.example.ktigerstudybe.model.GrammarTheory;
import org.example.ktigerstudybe.model.Lesson;
import org.example.ktigerstudybe.repository.GrammarTheoryRepository;
import org.example.ktigerstudybe.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrammarTheoryServiceImpl implements GrammarTheoryService {

    @Autowired
    private GrammarTheoryRepository grammarTheoryRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private GrammarTheoryResponse toResponse(GrammarTheory entity) {
        GrammarTheoryResponse resp = new GrammarTheoryResponse();
        resp.setGrammarId(entity.getGrammarId());
        resp.setLessonId(entity.getLesson().getLessonId());
        resp.setGrammarTitle(entity.getGrammarTitle());
        resp.setGrammarContent(entity.getGrammarContent());
        resp.setGrammarExample(entity.getGrammarExample());
        resp.setLevelId(entity.getLesson().getLevel().getLevelId());
        resp.setLevelName(entity.getLesson().getLevel().getLevelName());
        return resp;
    }

    private GrammarTheory toEntity(GrammarTheoryRequest req) {
        GrammarTheory entity = new GrammarTheory();
        Lesson lesson = lessonRepository.findById(req.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + req.getLessonId()));
        entity.setLesson(lesson);
        entity.setGrammarTitle(req.getGrammarTitle());
        entity.setGrammarContent(req.getGrammarContent());
        entity.setGrammarExample(req.getGrammarExample());
        return entity;
    }

    @Override
    public List<GrammarTheoryResponse> getAllGrammarTheories() {
        return grammarTheoryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GrammarTheoryResponse getGrammarTheoryById(Long id) {
        GrammarTheory entity = grammarTheoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GrammarTheory not found with id: " + id));
        return toResponse(entity);
    }

    @Override
    public GrammarTheoryResponse createGrammarTheory(GrammarTheoryRequest request) {
        GrammarTheory entity = toEntity(request);
        entity = grammarTheoryRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public GrammarTheoryResponse updateGrammarTheory(Long id, GrammarTheoryRequest request) {
        GrammarTheory entity = grammarTheoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GrammarTheory not found with id: " + id));
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + request.getLessonId()));
        entity.setLesson(lesson);
        entity.setGrammarTitle(request.getGrammarTitle());
        entity.setGrammarContent(request.getGrammarContent());
        entity.setGrammarExample(request.getGrammarExample());
        entity = grammarTheoryRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void deleteGrammarTheory(Long id) {
        grammarTheoryRepository.deleteById(id);
    }

    @Override
    public List<GrammarTheoryResponse> getGrammarByLessonId(Long lessonId) {
        return grammarTheoryRepository.findByLesson_LessonId(lessonId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<GrammarTheoryResponse> getGrammarByLevelId(Long levelId) {
        return grammarTheoryRepository.findByLevelId(levelId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    //admin
    // Đảm bảo truyền lessonId 2 lần
    @Override
    public Page<GrammarTheoryResponse> getPagedGrammarByLesson(Long lessonId, String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String actualSearchTerm = StringUtils.hasText(searchTerm) ? searchTerm.trim() : "";
        return grammarTheoryRepository
                .findByLesson_LessonIdAndGrammarTitleContainingIgnoreCaseOrLesson_LessonIdAndGrammarContentContainingIgnoreCase(
                        lessonId, actualSearchTerm, lessonId, actualSearchTerm, pageable
                )
                .map(this::toResponse);
    }
}