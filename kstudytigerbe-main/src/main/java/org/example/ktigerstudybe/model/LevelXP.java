package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "levelxp")
public class LevelXP {
    @Id
    @Column(name = "LevelNumber")
    private Integer levelNumber;

    @Column(name = "RequiredXP")
    private Integer requiredXP;

    @Column(name = "Title")
    private String title;

    @Column(name = "BadgeImage")
    private String badgeImage;
}
