package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByLesson_LessonId(Long lessonId);

    //ad
    Page<Exercise> findByLesson_LessonIdAndExerciseTitleContainingIgnoreCase(Long lessonId, String title, Pageable pageable);

}
