package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class DocumentItemRequest {

    private Long listId;       // ID của DocumentList (khóa ngoại)

    private String word;       // Từ vựng (bắt buộc)

    private String meaning;    // Nghĩa của từ (bắt buộc)

    private String example;    // Ví dụ (tùy chọn)

    private String vocabImage; // Ảnh minh họa (tùy chọn)
}
