package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_document_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDocumentList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FavoriteID")
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListID", nullable = false)
    private DocumentList documentList;

    @Column(name = "FavoriteAt", nullable = false, updatable = false)
    private LocalDateTime favoriteAt;

    @PrePersist
    protected void onFavorite() {
        this.favoriteAt = LocalDateTime.now();
    }
}
