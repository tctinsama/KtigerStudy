package org.example.ktigerstudybe.repository;


import org.example.ktigerstudybe.model.LevelXP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelXPRepository extends JpaRepository<LevelXP, Integer> {
    Optional<LevelXP> findByLevelNumber(Integer levelNumber);
}

