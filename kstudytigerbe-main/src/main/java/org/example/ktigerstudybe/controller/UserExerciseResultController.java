package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.UserExerciseResultRequest;
import org.example.ktigerstudybe.dto.resp.UserExerciseResultResponse;
import org.example.ktigerstudybe.service.userexerciseresult.UserExerciseResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-exercise-results")
public class UserExerciseResultController {

    @Autowired
    private UserExerciseResultService service;

    @PostMapping
    public UserExerciseResultResponse saveResult(@RequestBody UserExerciseResultRequest req) {
        return service.saveResult(req);
    }

    @GetMapping("/user/{userId}")
    public List<UserExerciseResultResponse> getByUser(@PathVariable Long userId) {
        return service.getResultsByUserId(userId);
    }

    @GetMapping("/user/{userId}/exercise/{exerciseId}")
    public UserExerciseResultResponse getByUserAndExercise(
            @PathVariable Long userId,
            @PathVariable Long exerciseId
    ) {
        return service.getResultByUserIdAndExerciseId(userId, exerciseId);
    }
}

