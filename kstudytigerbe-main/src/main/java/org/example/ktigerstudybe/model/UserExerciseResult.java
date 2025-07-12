// src/main/java/org/example/ktigerstudybe/model/UserExerciseResult.java
package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "userexerciseresult")
public class UserExerciseResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ResultID")
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ExerciseID")
    private Exercise exercise;

    @Column(name = "Score")
    private Integer score;

    @Column(name = "DateComplete")
    private LocalDateTime dateComplete;
}
