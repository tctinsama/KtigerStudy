package org.example.ktigerstudybe.service.documentItem;

import org.example.ktigerstudybe.dto.req.DocumentItemRequest;
import org.example.ktigerstudybe.dto.resp.DocumentItemResponse;
import org.example.ktigerstudybe.model.DocumentItem;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.repository.DocumentItemRepository;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentItemServiceImpl implements DocumentItemService {

    @Autowired
    private DocumentItemRepository documentItemRepository;

    @Autowired
    private DocumentListRepository documentListRepository;

    // Mapping entity -> response DTO
    private DocumentItemResponse toResponse(DocumentItem entity) {
        DocumentItemResponse resp = new DocumentItemResponse();
        resp.setWordId(entity.getWordId());
        resp.setListId(entity.getDocumentList().getListId());
        resp.setWord(entity.getWord());
        resp.setMeaning(entity.getMeaning());
        resp.setExample(entity.getExample());
        resp.setVocabImage(entity.getVocabImage());
        return resp;
    }

    // Mapping request DTO -> entity
    private DocumentItem toEntity(DocumentItemRequest request) {
        DocumentList list = documentListRepository.findById(request.getListId())
                .orElseThrow(() -> new IllegalArgumentException("DocumentList not found with id: " + request.getListId()));
        DocumentItem entity = new DocumentItem();
        entity.setDocumentList(list);
        entity.setWord(request.getWord());
        entity.setMeaning(request.getMeaning());
        entity.setExample(request.getExample());
        entity.setVocabImage(request.getVocabImage());
        return entity;
    }

    @Override
    public List<DocumentItemResponse> getAllDocumentItems() {
        return documentItemRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentItemResponse getDocumentItemById(Long wordId) {
        DocumentItem entity = documentItemRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("DocumentItem not found with id: " + wordId));
        return toResponse(entity);
    }

    @Override
    public List<DocumentItemResponse> getDocumentItemsByListId(Long listId) {
        return documentItemRepository.findByDocumentList_ListId(listId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentItemResponse createDocumentItem(DocumentItemRequest request) {
        DocumentItem entity = toEntity(request);
        entity = documentItemRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public DocumentItemResponse updateDocumentItem(Long wordId, DocumentItemRequest request) {
        DocumentItem entity = documentItemRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("DocumentItem not found with id: " + wordId));
        DocumentList list = documentListRepository.findById(request.getListId())
                .orElseThrow(() -> new IllegalArgumentException("DocumentList not found with id: " + request.getListId()));
        entity.setDocumentList(list);
        entity.setWord(request.getWord());
        entity.setMeaning(request.getMeaning());
        entity.setExample(request.getExample());
        entity.setVocabImage(request.getVocabImage());
        entity = documentItemRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void deleteDocumentItem(Long wordId) {
        documentItemRepository.deleteById(wordId);
    }

    @Override
    @Transactional
    public void deleteDocumentItemsByListId(Long listId) {
        documentItemRepository.deleteByDocumentList_ListId(listId);
    }

    //admin
    @Override
    public Page<DocumentItemResponse> getDocumentItemsPaged(
            Long listId, String keyword, Pageable pageable) {
        String kw = keyword == null ? "" : keyword.trim();
        Page<DocumentItem> page;
        if (kw.isEmpty()) {
            page = documentItemRepository.findByDocumentList_ListId(listId, pageable);
        } else {
            page = documentItemRepository.findByDocumentList_ListIdAndWordContainingIgnoreCase(
                    listId, kw, pageable
            );
        }
        return page.map(this::toResponse);
    }

}
