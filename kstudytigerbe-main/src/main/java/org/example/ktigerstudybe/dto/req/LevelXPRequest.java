package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class LevelXPRequest {
    private Integer levelNumber;
    private Integer requiredXP;
    private String title;
    private String badgeImage;
}
