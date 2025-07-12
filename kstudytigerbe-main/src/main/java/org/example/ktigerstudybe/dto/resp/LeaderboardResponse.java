package org.example.ktigerstudybe.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardResponse {
    private String fullName;
    private String currentTitle;
    private String currentBadge;
    private int totalXP;
}
