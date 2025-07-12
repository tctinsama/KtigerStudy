package org.example.ktigerstudybe.service.level;


import org.example.ktigerstudybe.dto.req.LevelRequest;
import org.example.ktigerstudybe.dto.resp.LevelResponse;

import java.util.List;

public interface LevelService {
    List<LevelResponse> getAllLevels();
    LevelResponse getLevelById(Long id);
    LevelResponse createLevel(LevelRequest request);
    LevelResponse updateLevel(Long id, LevelRequest request);
    void deleteLevel(Long id);
}