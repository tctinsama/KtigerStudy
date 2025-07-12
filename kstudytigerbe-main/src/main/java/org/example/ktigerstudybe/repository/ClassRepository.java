package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByUser_UserId(Long userId);
    Optional<ClassEntity> findByClassIdAndPassword(Long classId, String password);
}
