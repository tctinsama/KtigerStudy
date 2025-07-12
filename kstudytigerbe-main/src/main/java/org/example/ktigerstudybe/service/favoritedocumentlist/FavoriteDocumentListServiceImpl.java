package org.example.ktigerstudybe.service.favoritedocumentlist;

import org.example.ktigerstudybe.dto.req.FavoriteDocumentListRequest;
import org.example.ktigerstudybe.dto.resp.FavoriteDocumentListResponse;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.model.FavoriteDocumentList;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.example.ktigerstudybe.repository.FavoriteDocumentListRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteDocumentListServiceImpl implements FavoriteDocumentListService {

    @Autowired
    private FavoriteDocumentListRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DocumentListRepository listRepo;

    private FavoriteDocumentListResponse toResponse(FavoriteDocumentList fav) {
        FavoriteDocumentListResponse res = new FavoriteDocumentListResponse();
        res.setFavoriteId(fav.getFavoriteId());
        res.setUserId(fav.getUser().getUserId());
        res.setUserFullName(fav.getUser().getFullName());
        res.setListId(fav.getDocumentList().getListId());
        res.setListTitle(fav.getDocumentList().getTitle());
        res.setFavoriteAt(fav.getFavoriteAt());
        return res;
    }

    @Override
    public List<FavoriteDocumentListResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public FavoriteDocumentListResponse getById(Long id) {
        FavoriteDocumentList fav = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found favorite ID: " + id));
        return toResponse(fav);
    }

    @Override
    public FavoriteDocumentListResponse create(FavoriteDocumentListRequest request) {
        if (repo.findByUser_UserIdAndDocumentList_ListId(request.getUserId(), request.getListId()).isPresent()) {
            throw new IllegalArgumentException("Already favorited this list.");
        }

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        DocumentList list = listRepo.findById(request.getListId())
                .orElseThrow(() -> new IllegalArgumentException("DocumentList not found"));

        FavoriteDocumentList fav = FavoriteDocumentList.builder()
                .user(user)
                .documentList(list)
                .build();

        return toResponse(repo.save(fav));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Favorite not found");
        }
        repo.deleteById(id);
    }

    @Override
    public List<FavoriteDocumentListResponse> getByUser(Long userId) {
        return repo.findByUser_UserId(userId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FavoriteDocumentListResponse> getByList(Long listId) {
        return repo.findByDocumentList_ListId(listId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<FavoriteDocumentListResponse> getByUserAndList(Long userId, Long listId) {
        return repo.findByUser_UserIdAndDocumentList_ListId(userId, listId)
                .map(this::toResponse);
    }

}
