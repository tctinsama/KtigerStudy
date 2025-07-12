package org.example.ktigerstudybe.service.sentencerewritingquestion;

import org.example.ktigerstudybe.dto.req.SentenceRewritingQuestionRequest;
import org.example.ktigerstudybe.dto.resp.SentenceRewritingQuestionResponse;
import org.example.ktigerstudybe.model.Exercise;
import org.example.ktigerstudybe.model.SentenceRewritingQuestion;
import org.example.ktigerstudybe.repository.ExerciseRepository;
import org.example.ktigerstudybe.repository.SentenceRewritingQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SentenceRewritingQuestionServiceImpl implements SentenceRewritingQuestionService {

    @Autowired
    private SentenceRewritingQuestionRepository repo;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private SentenceRewritingQuestionResponse toResponse(SentenceRewritingQuestion entity) {
        SentenceRewritingQuestionResponse resp = new SentenceRewritingQuestionResponse();
        resp.setQuestionId(entity.getQuestionID());
        resp.setExerciseId(entity.getExercise().getExerciseId());
        resp.setOriginalSentence(entity.getOriginalSentence());
        resp.setRewrittenSentence(entity.getRewrittenSentence());
        resp.setLinkMedia(entity.getLinkMedia());
        return resp;
    }

    private SentenceRewritingQuestion toEntity(SentenceRewritingQuestionRequest req) {
        SentenceRewritingQuestion entity = new SentenceRewritingQuestion();
        Exercise exercise = exerciseRepository.findById(req.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + req.getExerciseId()));
        entity.setExercise(exercise);
        entity.setOriginalSentence(req.getOriginalSentence());
        entity.setRewrittenSentence(req.getRewrittenSentence());
        entity.setLinkMedia(req.getLinkMedia());
        return entity;
    }

    @Override
    public List<SentenceRewritingQuestionResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public SentenceRewritingQuestionResponse getById(Long id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));
    }

    @Override
    public SentenceRewritingQuestionResponse create(SentenceRewritingQuestionRequest request) {
        SentenceRewritingQuestion entity = toEntity(request);
        return toResponse(repo.save(entity));
    }

    @Override
    public SentenceRewritingQuestionResponse update(Long id, SentenceRewritingQuestionRequest request) {
        SentenceRewritingQuestion entity = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + request.getExerciseId()));
        entity.setExercise(exercise);
        entity.setOriginalSentence(request.getOriginalSentence());
        entity.setRewrittenSentence(request.getRewrittenSentence());
        entity.setLinkMedia(request.getLinkMedia());
        return toResponse(repo.save(entity));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<SentenceRewritingQuestionResponse> getByExerciseId(Long exerciseId) {
        return repo.findByExercise_ExerciseId(exerciseId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    //ad
    @Override
    public Page<SentenceRewritingQuestionResponse> getByLessonIdPaged(Long lessonId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String search = (keyword != null && !keyword.isEmpty()) ? keyword : "";
        return repo.findByExercise_Lesson_LessonIdAndOriginalSentenceContainingIgnoreCase(lessonId, search, pageable)
                .map(this::toResponse);
    }
}