package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class FavoriteDocumentListRequest {
    private Long userId;
    private Long listId;
}
