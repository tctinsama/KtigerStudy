package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "document_item")
public class DocumentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WordID")
    private Long wordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListID", nullable = false)
    private DocumentList documentList;

    @Column(name = "Word", nullable = false)
    private String word;

    @Column(name = "Meaning", nullable = false)
    private String meaning;

    @Column(name = "Example")
    private String example;

    @Column(name = "VocabImage")
    private String vocabImage;
}
