package org.example.ktigerstudybe.service.userexerciseresult;

import org.example.ktigerstudybe.dto.req.UserExerciseResultRequest;
import org.example.ktigerstudybe.dto.resp.UserExerciseResultResponse;
import org.example.ktigerstudybe.model.UserExerciseResult;

import java.util.List;

public interface UserExerciseResultService {
    UserExerciseResultResponse saveResult(UserExerciseResultRequest req);
    List<UserExerciseResultResponse> getResultsByUserId(Long userId);
    UserExerciseResultResponse getResultByUserIdAndExerciseId(Long userId, Long exerciseId);
}
