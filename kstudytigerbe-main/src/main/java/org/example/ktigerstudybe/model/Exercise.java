package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ExerciseID")
    private Long exerciseId;

    @ManyToOne
    @JoinColumn(name = "LessonID")
    private Lesson lesson;

    @Column(name = "ExerciseTitle")
    private String exerciseTitle;

    @Column(name = "ExerciseType")
    private String exerciseType;

    @Column(name = "ExerciseDescription")
    private String exerciseDescription;
}