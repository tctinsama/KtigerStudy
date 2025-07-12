package org.example.ktigerstudybe.service.levelxp;

import org.example.ktigerstudybe.dto.req.LevelXPRequest;
import org.example.ktigerstudybe.dto.resp.LevelXPResponse;

import java.util.List;

public interface LevelXPService {
    LevelXPResponse createOrUpdate(LevelXPRequest req);
    List<LevelXPResponse> getAll();
    LevelXPResponse getByLevelNumber(Integer levelNumber);
    void deleteByLevelNumber(Integer levelNumber);
}
