package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class UserXPResponse {
    private Long userXPId;
    private Long userId;
    private Integer totalXP;
    private Integer levelNumber;
    private String currentTitle;
    private String currentBadge;
}