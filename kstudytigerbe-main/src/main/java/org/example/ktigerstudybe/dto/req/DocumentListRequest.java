package org.example.ktigerstudybe.dto.req;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DocumentListRequest {

    private Long userId;

    private String title;

    private String description;

    private String type;

    private LocalDateTime createdAt;

    private int isPublic;
    private List<DocumentItemRequest> items;
}
