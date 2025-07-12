package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "document_report")
public class DocumentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReportID")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListID", nullable = false)
    private DocumentList documentList;

    @Column(name = "Reason", nullable = false)
    private String reason;

    @Column(name = "ReportDate", nullable = false, updatable = false)
    private LocalDateTime reportDate;

    @PrePersist
    protected void onCreate() {
        this.reportDate = LocalDateTime.now();
    }
}
