package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class LevelXPResponse {
    private Integer levelNumber;
    private Integer requiredXP;
    private String title;
    private String badgeImage;
}