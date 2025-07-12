package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.UserXPUpdateRequest;
import org.example.ktigerstudybe.dto.resp.LeaderboardResponse;
import org.example.ktigerstudybe.dto.resp.UserXPResponse;
import org.example.ktigerstudybe.service.userxp.UserXPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-xp")
public class UserXPController {

    @Autowired
    private UserXPService userXPService;

    @GetMapping("/{userId}")
    public UserXPResponse getUserXP(@PathVariable Long userId) {
        return userXPService.getUserXP(userId);
    }

    @PostMapping("/add")
    public UserXPResponse addXP(@RequestBody UserXPUpdateRequest req) {
        return userXPService.addXP(req);
    }
    @GetMapping("/leaderboard")
    public List<LeaderboardResponse> getLeaderboard() {
        return userXPService.getLeaderboard();
    }
}
