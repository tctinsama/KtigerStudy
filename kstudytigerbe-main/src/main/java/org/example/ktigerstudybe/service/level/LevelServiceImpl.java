package org.example.ktigerstudybe.service.level;


import org.example.ktigerstudybe.dto.req.LevelRequest;
import org.example.ktigerstudybe.dto.resp.LevelResponse;
import org.example.ktigerstudybe.model.Level;
import org.example.ktigerstudybe.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {

    @Autowired
    private LevelRepository levelRepository;

    private LevelResponse toResponse(Level level) {
        LevelResponse resp = new LevelResponse();
        resp.setLevelId(level.getLevelId());
        resp.setLevelName(level.getLevelName());
        resp.setDescription(level.getDescription());
        return resp;
    }

    private Level toEntity(LevelRequest request) {
        Level level = new Level();
        level.setLevelName(request.getLevelName());
        level.setDescription(request.getDescription());
        return level;
    }

    @Override
    public List<LevelResponse> getAllLevels() {
        return levelRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LevelResponse getLevelById(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Level not found with id: " + id));
        return toResponse(level);
    }

    @Override
    public LevelResponse createLevel(LevelRequest request) {
        Level level = toEntity(request);
        level = levelRepository.save(level);
        return toResponse(level);
    }

    @Override
    public LevelResponse updateLevel(Long id, LevelRequest request) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Level not found with id: " + id));
        level.setLevelName(request.getLevelName());
        level.setDescription(request.getDescription());
        level = levelRepository.save(level);
        return toResponse(level);
    }

    @Override
    public void deleteLevel(Long id) {
        levelRepository.deleteById(id);
    }
}
