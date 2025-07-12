package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "multiplechoicequestion")
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestionID")
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "ExerciseID")
    private Exercise exercise;

    @Column(name = "QuestionText")
    private String questionText;

    @Column(name = "OptionA")
    private String optionA;

    @Column(name = "OptionB")
    private String optionB;

    @Column(name = "OptionC")
    private String optionC;

    @Column(name = "OptionD")
    private String optionD;

    @Column(name = "CorrectAnswer")
    private String correctAnswer;

    @Column(name = "LinkMedia")
    private String linkMedia;
}
