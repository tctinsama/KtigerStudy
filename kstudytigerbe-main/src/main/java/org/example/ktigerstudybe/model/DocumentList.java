package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "document_list")
public class DocumentList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ListID")
    private Long listId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "Type")
    private String type;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "IsPublic", nullable = false)
    private int isPublic;

    // Phương thức này tự động gọi trước khi persist entity vào DB
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    @OneToMany(
            mappedBy = "documentList",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<DocumentReport> reports = new ArrayList<>();
}
