package org.example.ktigerstudybe.service.vocabularyTheory;

import org.example.ktigerstudybe.dto.req.VocabularyTheoryRequest;
import org.example.ktigerstudybe.dto.resp.VocabularyTheoryResponse;
import org.example.ktigerstudybe.model.Lesson;
import org.example.ktigerstudybe.model.VocabularyTheory;
import org.example.ktigerstudybe.repository.LessonRepository;
import org.example.ktigerstudybe.repository.LevelRepository;
import org.example.ktigerstudybe.repository.VocabularyTheoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocabularyTheoryServiceImpl implements VocabularyTheoryService {

    @Autowired
    private VocabularyTheoryRepository vocabularyTheoryRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private LevelRepository levelRepository;

    // Map entity -> response DTO
    private VocabularyTheoryResponse toResponse(VocabularyTheory vocab) {
        VocabularyTheoryResponse resp = new VocabularyTheoryResponse();
        resp.setVocabId(vocab.getVocabId());
        resp.setLessonId(vocab.getLesson().getLessonId()); // dòng này giúp FE biết lessonId
        resp.setWord(vocab.getWord());
        resp.setMeaning(vocab.getMeaning());
        resp.setExample(vocab.getExample());
        resp.setImage(vocab.getImage());

        //them
        resp.setLevelId(vocab.getLesson().getLevel().getLevelId());
        resp.setLevelName(vocab.getLesson().getLevel().getLevelName());
        return resp;
    }

    // Map request DTO -> entity
    private VocabularyTheory toEntity(VocabularyTheoryRequest req) {
        VocabularyTheory vocab = new VocabularyTheory();
        // Lấy lesson từ DB theo id (bắt buộc lessonId phải hợp lệ)
        Lesson lesson = lessonRepository.findById(req.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + req.getLessonId()));
        vocab.setLesson(lesson);
        vocab.setWord(req.getWord());
        vocab.setMeaning(req.getMeaning());
        vocab.setExample(req.getExample());
        vocab.setImage(req.getImage());
        return vocab;
    }

    @Override
    public List<VocabularyTheoryResponse> getAllVocabularyTheories() {
        return vocabularyTheoryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VocabularyTheoryResponse getVocabularyTheoryById(Long id) {
        VocabularyTheory vocab = vocabularyTheoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("VocabularyTheory not found with id: " + id));
        return toResponse(vocab);
    }

    @Override
    public VocabularyTheoryResponse createVocabularyTheory(VocabularyTheoryRequest request) {
        VocabularyTheory vocab = toEntity(request);
        vocab = vocabularyTheoryRepository.save(vocab);
        return toResponse(vocab);
    }

    @Override
    public VocabularyTheoryResponse updateVocabularyTheory(Long id, VocabularyTheoryRequest request) {
        VocabularyTheory vocab = vocabularyTheoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("VocabularyTheory not found with id: " + id));
        // Update fields
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + request.getLessonId()));
        vocab.setLesson(lesson);
        vocab.setWord(request.getWord());
        vocab.setMeaning(request.getMeaning());
        vocab.setExample(request.getExample());
        vocab.setImage(request.getImage());

        vocab = vocabularyTheoryRepository.save(vocab);
        return toResponse(vocab);
    }

    @Override
    public void deleteVocabularyTheory(Long id) {
        vocabularyTheoryRepository.deleteById(id);
    }

    @Override
    public List<VocabularyTheoryResponse> getVocabulariesByLessonId(Long lessonId) {
        return vocabularyTheoryRepository.findByLesson_LessonId(lessonId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VocabularyTheoryResponse> getVocabulariesByLevelId(Long levelId) {
        return vocabularyTheoryRepository.findByLevelId(levelId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    //admin
    @Override
    public Page<VocabularyTheoryResponse> getPagedVocabByLesson(Long lessonId, String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Sử dụng phương thức repository có tìm kiếm
        String actualSearchTerm = StringUtils.hasText(searchTerm) ? searchTerm : ""; // Pass empty string if no search term
        return vocabularyTheoryRepository.findByLesson_LessonIdAndWordContainingIgnoreCaseOrMeaningContainingIgnoreCase(
                lessonId, actualSearchTerm, actualSearchTerm, pageable
        ).map(this::toResponse);
    }



}
