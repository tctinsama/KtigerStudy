// src/main/java/org/example/ktigerstudybe/service/documentList/DocumentListServiceImpl.java
package org.example.ktigerstudybe.service.documentList;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.mapper.DocumentListMapper;
import org.example.ktigerstudybe.model.DocumentItem;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.DocumentItemRepository;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.example.ktigerstudybe.repository.FavoriteDocumentListRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DocumentListServiceImpl implements DocumentListService {

    private final DocumentListRepository documentListRepository;
    private final UserRepository userRepository;
    private final DocumentItemRepository documentItemRepository;
    private final DocumentListMapper mapper;
    @Autowired
    private FavoriteDocumentListRepository repo;

    @Autowired
    public DocumentListServiceImpl(
            DocumentListRepository documentListRepository,
            UserRepository userRepository,
            DocumentItemRepository documentItemRepository,
            DocumentListMapper mapper
    ) {
        this.documentListRepository = documentListRepository;
        this.userRepository = userRepository;
        this.documentItemRepository = documentItemRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public DocumentListResponse createDocumentList(DocumentListRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found: " + request.getUserId())
                );

        DocumentList list = DocumentList.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .isPublic(request.getIsPublic())
                .build();
        list = documentListRepository.save(list);

        if (request.getItems() != null) {
            for (var it : request.getItems()) {
                DocumentItem item = DocumentItem.builder()
                        .documentList(list)
                        .word(it.getWord())
                        .meaning(it.getMeaning())
                        .example(it.getExample())
                        .vocabImage(it.getVocabImage())
                        .build();
                documentItemRepository.save(item);
            }
        }
        return mapper.toResponse(list);
    }

    @Override
    public List<DocumentListResponse> getAllDocumentLists() {
        return documentListRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentListResponse> getPublicLists() {
        return documentListRepository.findAllByIsPublic(0)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DocumentListResponse> getPublicLists(Pageable pageable) {
        return documentListRepository.findByIsPublic(0, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public DocumentListResponse getDocumentListById(Long id) {
        DocumentList e = documentListRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("DocumentList not found: " + id)
                );
        return mapper.toResponse(e);
    }

    @Override
    public Page<DocumentListResponse> searchPublicByTitleOrType(String keyword, Pageable pageable) {
        String kw = (keyword == null ? "" : keyword.trim());
        return documentListRepository
                // nếu dùng @Query:
                .searchPublicByTitleOrType(kw, pageable)
                // nếu dùng method tự sinh:
                // .findByIsPublicAndTitleContainingIgnoreCaseOrIsPublicAndTypeContainingIgnoreCase(0, kw, 0, kw, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public void toggleVisibility(Long id) {
        DocumentList list = documentListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("DocumentList not found: " + id));

        // flip 0 ↔ 1
        list.setIsPublic(list.getIsPublic() == 0 ? 1 : 0);

        // save the change
        documentListRepository.save(list);
    }

    @Override
    @Transactional
    public DocumentListResponse updateDocumentList(Long id, DocumentListRequest req) {
        DocumentList e = documentListRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("DocumentList not found: " + id)
                );
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setType(req.getType());
        e.setIsPublic(req.getIsPublic());
        DocumentList updated = documentListRepository.save(e);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteDocumentList(Long id) {
        if (!documentListRepository.existsById(id)) {
            throw new NoSuchElementException("Cannot delete, not found: " + id);
        }

        // 1) Xóa các favorite trỏ tới list này
        repo.deleteByDocumentList_ListId(id);

        // 2) Xóa các document items trỏ tới list này
        documentItemRepository.deleteByDocumentList_ListId(id);

        // 3) Cuối cùng xóa document list
        documentListRepository.deleteById(id);
    }

    @Override
    public List<DocumentListResponse> getDocumentListFavoritedByUserId(Long id){
        return documentListRepository.findFavoritedByUserId(id).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentListResponse> getUnassignedByUserId(Long userId) {
        return documentListRepository
                .findUnassignedByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }



    @Override
    public List<DocumentListResponse> getDocumentListsByUserId(Long userId) {
        return documentListRepository
                .findByUser_UserIdOrderByCreatedAtDesc(userId)   // ← method đã tồn tại
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentListResponse> getByTypeAndPublic(String type, int isPublic) {
        return documentListRepository.findByTypeAndIsPublic(type, isPublic)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DocumentListResponse>> getGroupedByType(int limit) {
        List<String> types = documentListRepository.findDistinctTypes();
        Map<String, List<DocumentListResponse>> map = new LinkedHashMap<>();
        for (String t : types) {
            List<DocumentListResponse> slice = documentListRepository
                    .findByTypeAndIsPublic(t, 0)
                    .stream()
                    .limit(limit)
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
            map.put(t, slice);
        }
        return map;
    }


    //amdin
    @Override
    public Page<DocumentListResponse> listByUser(Long userId, Pageable pg) {
        // Lấy tất cả tài liệu của user
        return documentListRepository
                .findByUser_UserId(userId, pg)
                .map(mapper::toResponse);
    }

    @Override
    public Page<DocumentListResponse> searchPublic(String keyword, Pageable pageable) {
        String kw = (keyword == null ? "" : keyword.trim());

        if (kw.isEmpty()) {
            // Không có keyword => lấy tất cả tài liệu
            return documentListRepository
                    .findAll(pageable)
                    .map(mapper::toResponse);
        } else {
            // Có keyword => tìm theo title hoặc tên tác giả
            return documentListRepository
                    .findByTitleContainingIgnoreCaseOrUser_FullNameContainingIgnoreCase(
                            kw, kw, pageable
                    )
                    .map(mapper::toResponse);
        }
    }

}
