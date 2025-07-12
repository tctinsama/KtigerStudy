package org.example.ktigerstudybe.repository;

import jakarta.transaction.Transactional;
import org.example.ktigerstudybe.model.ClassUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface ClassUserRepository extends JpaRepository<ClassUser, Long> {
    List<ClassUser> findByUser_UserId(Long userId);
    List<ClassUser> findByClassEntity_ClassId(Long classId);
    // Hàm xoá tất cả ClassUser theo userId
    @Modifying
    @Transactional
    void deleteAllByUser_UserId(Long userId);
}
