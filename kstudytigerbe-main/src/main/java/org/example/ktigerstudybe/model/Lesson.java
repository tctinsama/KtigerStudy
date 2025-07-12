package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LessonID")
    private Long lessonId;

    @Column(name = "LessonName")
    private String lessonName;

    @Column(name = "LessonDescription")
    private String lessonDescription;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<VocabularyTheory> vocabularies;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<GrammarTheory> grammars;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LevelID", referencedColumnName = "LevelID")
    private Level level;

}
