package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "grammartheory")
public class GrammarTheory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GrammarID")
    private Long grammarId;

    @ManyToOne
    @JoinColumn(name = "LessonID")
    private Lesson lesson;

    @Column(name = "GrammarTitle")
    private String grammarTitle;

    @Column(name = "GrammarContent")
    private String grammarContent;

    @Column(name = "GrammarExample")
    private String grammarExample;

}

