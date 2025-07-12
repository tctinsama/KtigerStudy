package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "userxp")
public class UserXP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserXPID")
    private Long userXPId;

    @OneToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @Column(name = "TotalXP")
    private Integer totalXP;

    @Column(name = "LevelNumber")
    private Integer levelNumber;

    @Column(name = "CurrentTitle")
    private String currentTitle;

    @Column(name = "CurrentBadge")
    private String currentBadge;
}