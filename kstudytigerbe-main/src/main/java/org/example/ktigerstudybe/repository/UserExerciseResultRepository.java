package org.example.ktigerstudybe.repository;


import org.example.ktigerstudybe.model.UserExerciseResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserExerciseResultRepository extends JpaRepository<UserExerciseResult, Long> {
    List<UserExerciseResult> findByUser_UserId(Long userId);
    UserExerciseResult findByUser_UserIdAndExercise_ExerciseId(Long userId, Long exerciseId);
}