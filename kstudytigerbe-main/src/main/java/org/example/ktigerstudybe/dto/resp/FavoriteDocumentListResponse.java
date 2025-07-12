package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteDocumentListResponse {
    private Long favoriteId;
    private Long userId;
    private String userFullName;
    private Long listId;
    private String listTitle;
    private LocalDateTime favoriteAt;
}
