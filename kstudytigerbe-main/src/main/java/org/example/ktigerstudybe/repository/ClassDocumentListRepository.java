package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ClassDocumentList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassDocumentListRepository extends JpaRepository<ClassDocumentList, Long> {
    List<ClassDocumentList> findByClassEntity_ClassId(Long classId);
    List<ClassDocumentList> findByDocumentList_ListId(Long listId);
}
