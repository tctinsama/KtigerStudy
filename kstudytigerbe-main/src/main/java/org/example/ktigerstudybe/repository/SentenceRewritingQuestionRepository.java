package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.SentenceRewritingQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentenceRewritingQuestionRepository extends JpaRepository<SentenceRewritingQuestion, Long> {
    List<SentenceRewritingQuestion> findByExercise_ExerciseId(Long exerciseId);

    Page<SentenceRewritingQuestion> findByExercise_Lesson_LessonIdAndOriginalSentenceContainingIgnoreCase(Long lessonId, String keyword, Pageable pageable);
}
