package org.example.ktigerstudybe.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentReportResponse {
    private Long reportId;
    private Long userId;
    private String userName; // Optional: thêm thông tin người dùng
    private Long listId;
    private String listTitle; // Optional: thêm tiêu đề tài liệu
    private String reason;
    private LocalDateTime reportDate;
}
