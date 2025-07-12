// src/main/java/org/example/ktigerstudybe/service/classDocumentList/ClassDocumentListServiceImpl.java
package org.example.ktigerstudybe.service.classDocumentList;

import org.example.ktigerstudybe.dto.req.ClassDocumentListRequest;
import org.example.ktigerstudybe.dto.resp.ClassDocumentListResponse;
import org.example.ktigerstudybe.model.ClassDocumentList;
import org.example.ktigerstudybe.model.ClassEntity;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.repository.ClassDocumentListRepository;
import org.example.ktigerstudybe.repository.ClassRepository;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassDocumentListServiceImpl implements ClassDocumentListService {

    @Autowired
    private ClassDocumentListRepository repo;

    @Autowired
    private ClassRepository classRepo;

    @Autowired
    private DocumentListRepository listRepo;

    private ClassDocumentListResponse toResponse(ClassDocumentList cd) {
        ClassDocumentListResponse res = new ClassDocumentListResponse();
        res.setClassDocumentListId(cd.getClassDocumentListId());
        res.setClassId(cd.getClassEntity().getClassId());
        res.setClassName(cd.getClassEntity().getClassName());
        DocumentList dl = cd.getDocumentList();
        res.setListId(dl.getListId());
        res.setListTitle(dl.getTitle());
        res.setAssignedAt(cd.getAssignedAt());

        // new fields:
        res.setFullName(dl.getUser().getFullName());
        res.setAvatarImage(dl.getUser().getAvatarImage());
        res.setDescription(dl.getDescription());
        res.setType(dl.getType());
        return res;
    }

    @Override
    public List<ClassDocumentListResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClassDocumentListResponse getById(Long id) {
        ClassDocumentList cd = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found ClassDocumentListID: " + id));
        return toResponse(cd);
    }

    @Override
    public ClassDocumentListResponse create(ClassDocumentListRequest request) {
        ClassEntity classEntity = classRepo.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        DocumentList documentList = listRepo.findById(request.getListId())
                .orElseThrow(() -> new IllegalArgumentException("DocumentList not found"));

        ClassDocumentList cd = ClassDocumentList.builder()
                .classEntity(classEntity)
                .documentList(documentList)
                .build();

        return toResponse(repo.save(cd));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete: Not found ID " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public List<ClassDocumentListResponse> getByClassId(Long classId) {
        return repo.findByClassEntity_ClassId(classId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassDocumentListResponse> getByListId(Long listId) {
        return repo.findByDocumentList_ListId(listId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
