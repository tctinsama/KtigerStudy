package org.example.ktigerstudybe.service.chat;

import org.example.ktigerstudybe.dto.req.CreateChatConversationRequest;
import org.example.ktigerstudybe.dto.req.SendChatMessageRequest;
import org.example.ktigerstudybe.dto.resp.ChatConversationResponse;
import org.example.ktigerstudybe.dto.resp.ChatMessageResponse;
import org.example.ktigerstudybe.dto.resp.ChatResponsePair;

import java.util.List;

public interface ChatService {
    ChatConversationResponse createConversation(CreateChatConversationRequest request);
    ChatResponsePair sendMessage(Long conversationId, SendChatMessageRequest request);
    List<ChatMessageResponse> getConversationMessages(Long conversationId);
    List<ChatConversationResponse> getUserConversations(Long userId);
    void deleteConversation(Long conversationId) ;
}