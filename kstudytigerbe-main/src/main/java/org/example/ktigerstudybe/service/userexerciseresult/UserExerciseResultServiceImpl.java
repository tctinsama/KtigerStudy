package org.example.ktigerstudybe.service.userexerciseresult;


import org.example.ktigerstudybe.dto.req.UserExerciseResultRequest;
import org.example.ktigerstudybe.dto.resp.UserExerciseResultResponse;
import org.example.ktigerstudybe.model.Exercise;
import org.example.ktigerstudybe.model.UserExerciseResult;
import org.example.ktigerstudybe.repository.ExerciseRepository;
import org.example.ktigerstudybe.repository.UserExerciseResultRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserExerciseResultServiceImpl implements UserExerciseResultService {

    @Autowired
    private UserExerciseResultRepository userExerciseResultRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE_TIME;

    private UserExerciseResultResponse toResponse(UserExerciseResult entity) {
        UserExerciseResultResponse resp = new UserExerciseResultResponse();
        resp.setResultId(entity.getResultId());
        resp.setUserId(entity.getUser().getUserId());    // CHỈNH SỬA chỗ này
        resp.setExerciseId(entity.getExercise().getExerciseId());
        resp.setScore(entity.getScore());
        resp.setDateComplete(entity.getDateComplete().toString());
        return resp;
    }

    @Override
    public UserExerciseResultResponse saveResult(UserExerciseResultRequest req) {
        UserExerciseResult entity = new UserExerciseResult();

        // Lấy entity User
        var user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Lấy entity Exercise
        var exercise = exerciseRepository.findById(req.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        // Set entity
        entity.setUser(user);                // CHỈNH SỬA chỗ này
        entity.setExercise(exercise);
        entity.setScore(req.getScore());
        entity.setDateComplete(LocalDateTime.parse(req.getDateComplete(), ISO));

        // Lưu kết quả
        entity = userExerciseResultRepository.save(entity);
        return toResponse(entity);
    }


    @Override
    public List<UserExerciseResultResponse> getResultsByUserId(Long userId) {
        return userExerciseResultRepository.findByUser_UserId(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public UserExerciseResultResponse getResultByUserIdAndExerciseId(Long userId, Long exerciseId) {
        return Optional.ofNullable(
                userExerciseResultRepository.findByUser_UserIdAndExercise_ExerciseId(userId, exerciseId)
        ).map(this::toResponse).orElse(null);
    }

}