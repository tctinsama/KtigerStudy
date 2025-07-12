package org.example.ktigerstudybe.repository;

import jakarta.transaction.Transactional;
import org.example.ktigerstudybe.model.FavoriteDocumentList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteDocumentListRepository extends JpaRepository<FavoriteDocumentList, Long> {
    List<FavoriteDocumentList> findByUser_UserId(Long userId);
    List<FavoriteDocumentList> findByDocumentList_ListId(Long listId);
    Optional<FavoriteDocumentList> findByUser_UserIdAndDocumentList_ListId(Long userId, Long listId);
    /** Xóa tất cả favorite liên quan đến một documentList */
    @Modifying
    @Query("DELETE FROM FavoriteDocumentList f WHERE f.documentList.listId = :listId")
    void deleteByDocumentList_ListId(@Param("listId") Long listId);

}
