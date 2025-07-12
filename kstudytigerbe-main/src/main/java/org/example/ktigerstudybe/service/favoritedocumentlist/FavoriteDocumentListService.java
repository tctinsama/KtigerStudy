package org.example.ktigerstudybe.service.favoritedocumentlist;

import org.example.ktigerstudybe.dto.req.FavoriteDocumentListRequest;
import org.example.ktigerstudybe.dto.resp.FavoriteDocumentListResponse;

import java.util.List;
import java.util.Optional;

public interface FavoriteDocumentListService {
    List<FavoriteDocumentListResponse> getAll();
    FavoriteDocumentListResponse getById(Long id);
    FavoriteDocumentListResponse create(FavoriteDocumentListRequest request);
    void delete(Long id);
    List<FavoriteDocumentListResponse> getByUser(Long userId);
    List<FavoriteDocumentListResponse> getByList(Long listId);
    Optional<FavoriteDocumentListResponse> getByUserAndList(Long userId, Long listId);
}
