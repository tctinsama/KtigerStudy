package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Lesson;
import org.example.ktigerstudybe.model.UserProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByLevel_LevelId(Long levelId);


    Page<Lesson> findByLevel_LevelId(Long levelId, Pageable pageable);

    Page<Lesson> findByLessonNameContainingIgnoreCaseAndLevel_LevelId(
            String lessonName, Long levelId, Pageable pageable);


    Page<Lesson> findByLessonNameContainingIgnoreCase(
            String lessonName, Pageable pageable);

}