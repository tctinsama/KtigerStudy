package org.example.ktigerstudybe.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vocabularytheory")
public class VocabularyTheory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VocabID")
    private Long vocabId;

    @ManyToOne
    @JoinColumn(name = "LessonID")
    private Lesson lesson;

    @Column(name = "Word")
    private String word;

    @Column(name = "Meaning")
    private String meaning;

    @Column(name = "Example")
    private String example;

    @Column(name = "Image")
    private String image;

}

