package org.example.ktigerstudybe.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentReportRequest {
    private Long userId;     // ID của người dùng thực hiện báo cáo
    private Long listId;     // ID của tài liệu bị báo cáo
    private String reason;   // Lý do báo cáo
}
