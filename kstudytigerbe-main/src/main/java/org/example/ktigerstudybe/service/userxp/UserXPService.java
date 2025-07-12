package org.example.ktigerstudybe.service.userxp;

import org.example.ktigerstudybe.dto.req.UserXPUpdateRequest;
import org.example.ktigerstudybe.dto.resp.LeaderboardResponse;
import org.example.ktigerstudybe.dto.resp.UserXPResponse;

import java.util.List;

public interface UserXPService {
    UserXPResponse getUserXP(Long userId);
    UserXPResponse addXP(UserXPUpdateRequest req);
    UserXPResponse createInitialUserXP(Long userId);
    List<LeaderboardResponse> getLeaderboard();

}