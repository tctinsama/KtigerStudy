package org.example.ktigerstudybe.service.levelxp;

import org.example.ktigerstudybe.dto.req.LevelXPRequest;
import org.example.ktigerstudybe.dto.resp.LevelXPResponse;
import org.example.ktigerstudybe.model.LevelXP;
import org.example.ktigerstudybe.repository.LevelXPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelXPServiceImpl implements LevelXPService {

    @Autowired
    private LevelXPRepository levelXPRepository;

    private LevelXPResponse toResponse(LevelXP entity) {
        LevelXPResponse resp = new LevelXPResponse();
        resp.setLevelNumber(entity.getLevelNumber());
        resp.setRequiredXP(entity.getRequiredXP());
        resp.setTitle(entity.getTitle());
        resp.setBadgeImage(entity.getBadgeImage());
        return resp;
    }

    @Override
    public LevelXPResponse createOrUpdate(LevelXPRequest req) {
        LevelXP entity = new LevelXP();
        entity.setLevelNumber(req.getLevelNumber());
        entity.setRequiredXP(req.getRequiredXP());
        entity.setTitle(req.getTitle());
        entity.setBadgeImage(req.getBadgeImage());
        entity = levelXPRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public List<LevelXPResponse> getAll() {
        return levelXPRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LevelXPResponse getByLevelNumber(Integer levelNumber) {
        LevelXP entity = levelXPRepository.findByLevelNumber(levelNumber)
                .orElseThrow(() -> new IllegalArgumentException("Level not found"));
        return toResponse(entity);
    }

    @Override
    public void deleteByLevelNumber(Integer levelNumber) {
        levelXPRepository.deleteById(levelNumber);
    }
}