package org.example.ktigerstudybe.repository;


import org.example.ktigerstudybe.model.UserXP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserXPRepository extends JpaRepository<UserXP, Long> {
    Optional<UserXP> findByUser_UserId(Long userId);
    @Query("SELECT u FROM UserXP u JOIN FETCH u.user ORDER BY u.totalXP DESC")
    List<UserXP> findAllWithUserOrderedByXP();
}