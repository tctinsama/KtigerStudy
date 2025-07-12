package org.example.ktigerstudybe.dto.resp;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private Long messageId;
    private Long conversationId;
    private String content;
    private String messageType;
    private LocalDateTime timestamp;

    private String translation;



    // Constructors
    public ChatMessageResponse() {}

    public ChatMessageResponse(Long messageId, Long conversationId, String content,
                               String messageType, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.content = content;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

}