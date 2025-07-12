package org.example.ktigerstudybe.dto.resp;

public class ChatResponsePair {
    private ChatMessageResponse userMessage;
    private ChatMessageResponse aiMessage;

    // Constructors
    public ChatResponsePair() {}

    public ChatResponsePair(ChatMessageResponse userMessage, ChatMessageResponse aiMessage) {
        this.userMessage = userMessage;
        this.aiMessage = aiMessage;
    }

    // Getters and Setters
    public ChatMessageResponse getUserMessage() { return userMessage; }
    public void setUserMessage(ChatMessageResponse userMessage) { this.userMessage = userMessage; }

    public ChatMessageResponse getAiMessage() { return aiMessage; }
    public void setAiMessage(ChatMessageResponse aiMessage) { this.aiMessage = aiMessage; }
}