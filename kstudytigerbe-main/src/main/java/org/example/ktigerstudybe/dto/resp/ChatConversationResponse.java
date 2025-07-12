package org.example.ktigerstudybe.dto.resp;

import java.time.LocalDateTime;

public class ChatConversationResponse {
    private Long conversationId;
    private Long userId;
    private String title;
    private String scenario;
    private String difficulty;
    private LocalDateTime createdAt;
    private int messageCount;

    // Constructors
    public ChatConversationResponse() {}

    public ChatConversationResponse(Long conversationId, Long userId, String title,
                                    String scenario, String difficulty, LocalDateTime createdAt,
                                    int messageCount) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.title = title;
        this.scenario = scenario;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
        this.messageCount = messageCount;
    }

    // Getters and Setters
    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getScenario() { return scenario; }
    public void setScenario(String scenario) { this.scenario = scenario; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int messageCount) { this.messageCount = messageCount; }
}