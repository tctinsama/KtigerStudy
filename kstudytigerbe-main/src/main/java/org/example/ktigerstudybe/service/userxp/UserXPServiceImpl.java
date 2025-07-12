package org.example.ktigerstudybe.service.userxp;

import org.example.ktigerstudybe.dto.req.UserXPUpdateRequest;
import org.example.ktigerstudybe.dto.resp.LeaderboardResponse;
import org.example.ktigerstudybe.dto.resp.UserXPResponse;
import org.example.ktigerstudybe.model.LevelXP;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.model.UserXP;
import org.example.ktigerstudybe.repository.LevelXPRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.example.ktigerstudybe.repository.UserXPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserXPServiceImpl implements UserXPService {

    @Autowired
    private UserXPRepository userXPRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LevelXPRepository levelXPRepository;

    private UserXPResponse toResponse(UserXP entity) {
        UserXPResponse resp = new UserXPResponse();
        resp.setUserXPId(entity.getUserXPId());
        resp.setUserId(entity.getUser().getUserId());
        resp.setTotalXP(entity.getTotalXP());
        resp.setLevelNumber(entity.getLevelNumber());
        resp.setCurrentTitle(entity.getCurrentTitle());
        resp.setCurrentBadge(entity.getCurrentBadge());
        return resp;
    }

    @Override
    public UserXPResponse getUserXP(Long userId) {
        UserXP userXP = userXPRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserXP not found!"));
        return toResponse(userXP);
    }

    @Override
    public UserXPResponse addXP(UserXPUpdateRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        // Tìm hoặc tạo UserXP
        UserXP userXP = userXPRepository.findByUser_UserId(user.getUserId())
                .orElseGet(() -> {
                    UserXP ux = new UserXP();
                    ux.setUser(user);
                    ux.setTotalXP(0);
                    ux.setLevelNumber(1);
                    // Gán title/badge level 1 nếu có
                    LevelXP level1 = levelXPRepository.findByLevelNumber(1).orElse(null);
                    if (level1 != null) {
                        ux.setCurrentTitle(level1.getTitle());
                        ux.setCurrentBadge(level1.getBadgeImage());
                    }
                    return ux;
                });

        int newTotalXP = userXP.getTotalXP() + req.getXpToAdd();
        userXP.setTotalXP(newTotalXP);

        // Xử lý lên level nếu đạt XP mới
        int curLevel = userXP.getLevelNumber();
        Optional<LevelXP> nextLevelOpt = levelXPRepository.findByLevelNumber(curLevel + 1);

        while (nextLevelOpt.isPresent() && newTotalXP >= nextLevelOpt.get().getRequiredXP()) {
            LevelXP nextLevel = nextLevelOpt.get();
            userXP.setLevelNumber(nextLevel.getLevelNumber());
            userXP.setCurrentTitle(nextLevel.getTitle());
            userXP.setCurrentBadge(nextLevel.getBadgeImage());
            curLevel = nextLevel.getLevelNumber();
            nextLevelOpt = levelXPRepository.findByLevelNumber(curLevel + 1);
        }

        userXP = userXPRepository.save(userXP);
        return toResponse(userXP);
    }

    @Override
    public UserXPResponse createInitialUserXP(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LevelXP level1 = levelXPRepository.findByLevelNumber(1)
                .orElseThrow(() -> new IllegalStateException("Level 1 data missing"));

        UserXP userXP = new UserXP();
        userXP.setUser(user);
        userXP.setTotalXP(0);
        userXP.setLevelNumber(level1.getLevelNumber());
        userXP.setCurrentTitle(level1.getTitle());
        userXP.setCurrentBadge(level1.getBadgeImage());

        userXP = userXPRepository.save(userXP);

        return toResponse(userXP);
    }

    @Override
    public List<LeaderboardResponse> getLeaderboard() {
        List<UserXP> userXPList = userXPRepository.findAllWithUserOrderedByXP();
        return userXPList.stream()
                .map(u ->
                        new LeaderboardResponse(
                        u.getUser().getFullName(),
                        u.getCurrentTitle(),
                        u.getCurrentBadge(),
                        u.getTotalXP()
                )).toList();
    }
}

