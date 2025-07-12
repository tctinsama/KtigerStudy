package org.example.ktigerstudybe.service.multiplechoicequestion;

import org.example.ktigerstudybe.dto.req.MultipleChoiceQuestionRequest;
import org.example.ktigerstudybe.dto.resp.MultipleChoiceQuestionResponse;
import org.example.ktigerstudybe.model.Exercise;
import org.example.ktigerstudybe.model.MultipleChoiceQuestion;
import org.example.ktigerstudybe.repository.ExerciseRepository;
import org.example.ktigerstudybe.repository.MultipleChoiceQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultipleChoiceQuestionServiceImpl implements MultipleChoiceQuestionService {

    @Autowired
    private MultipleChoiceQuestionRepository questionRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private MultipleChoiceQuestionResponse toResponse(MultipleChoiceQuestion q) {
        MultipleChoiceQuestionResponse resp = new MultipleChoiceQuestionResponse();
        resp.setQuestionId(q.getQuestionId());
        resp.setExerciseId(q.getExercise().getExerciseId());
        resp.setQuestionText(q.getQuestionText());
        resp.setOptionA(q.getOptionA());
        resp.setOptionB(q.getOptionB());
        resp.setOptionC(q.getOptionC());
        resp.setOptionD(q.getOptionD());
        resp.setCorrectAnswer(q.getCorrectAnswer());
        resp.setLinkMedia(q.getLinkMedia());
        return resp;
    }

    private MultipleChoiceQuestion toEntity(MultipleChoiceQuestionRequest req) {
        Exercise exercise = exerciseRepository.findById(req.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + req.getExerciseId()));

        MultipleChoiceQuestion q = new MultipleChoiceQuestion();
        q.setExercise(exercise);
        q.setQuestionText(req.getQuestionText());
        q.setOptionA(req.getOptionA());
        q.setOptionB(req.getOptionB());
        q.setOptionC(req.getOptionC());
        q.setOptionD(req.getOptionD());
        q.setCorrectAnswer(req.getCorrectAnswer());
        q.setLinkMedia(req.getLinkMedia());
        return q;
    }

    @Override
    public List<MultipleChoiceQuestionResponse> getAll() {
        return questionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public MultipleChoiceQuestionResponse getById(Long id) {
        return questionRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));
    }

    @Override
    public MultipleChoiceQuestionResponse create(MultipleChoiceQuestionRequest request) {
        MultipleChoiceQuestion q = toEntity(request);
        q = questionRepository.save(q);
        return toResponse(q);
    }

    @Override
    public MultipleChoiceQuestionResponse update(Long id, MultipleChoiceQuestionRequest request) {
        MultipleChoiceQuestion q = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));

        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + request.getExerciseId()));

        q.setExercise(exercise);
        q.setQuestionText(request.getQuestionText());
        q.setOptionA(request.getOptionA());
        q.setOptionB(request.getOptionB());
        q.setOptionC(request.getOptionC());
        q.setOptionD(request.getOptionD());
        q.setCorrectAnswer(request.getCorrectAnswer());
        q.setLinkMedia(request.getLinkMedia());
        q = questionRepository.save(q);
        return toResponse(q);
    }

    @Override
    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public List<MultipleChoiceQuestionResponse> getByExerciseId(Long exerciseId) {
        return questionRepository.findByExercise_ExerciseId(exerciseId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    //admin
    @Override
    public Page<MultipleChoiceQuestionResponse> getByLessonIdPaged(Long lessonId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String search = (keyword != null && !keyword.isEmpty()) ? keyword : "";
        return questionRepository
                .findByExercise_Lesson_LessonIdAndQuestionTextContainingIgnoreCase(lessonId, search, pageable)
                .map(this::toResponse);
    }
}