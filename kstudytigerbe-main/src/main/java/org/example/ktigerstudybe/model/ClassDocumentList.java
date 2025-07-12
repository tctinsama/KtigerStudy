package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "class_document_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDocumentList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClassDocumentListID")
    private Long classDocumentListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClassID", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListID", nullable = false)
    private DocumentList documentList;

    @Column(name = "AssignedAt", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onAssign() {
        this.assignedAt = LocalDateTime.now();
    }
}
