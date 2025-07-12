package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class DocumentItemResponse {

    private Long wordId;       // ID của từ vựng (PK)

    private Long listId;       // ID của danh sách tài liệu

    private String word;       // Từ vựng

    private String meaning;    // Nghĩa

    private String example;    // Ví dụ (có thể null)

    private String vocabImage; // Ảnh minh họa (có thể null)
}
