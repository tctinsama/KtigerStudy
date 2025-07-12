package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.CreateChatConversationRequest;
import org.example.ktigerstudybe.dto.req.SendChatMessageRequest;
import org.example.ktigerstudybe.dto.resp.ChatConversationResponse;
import org.example.ktigerstudybe.dto.resp.ChatMessageResponse;
import org.example.ktigerstudybe.dto.resp.ChatResponsePair;
import org.example.ktigerstudybe.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@Validated
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 새로운 대화 생성
     */
    @PostMapping("/conversations")
    public ResponseEntity<ChatConversationResponse> createConversation(
            @Valid @RequestBody CreateChatConversationRequest request) {
        try {
            ChatConversationResponse response = chatService.createConversation(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 메시지 전송 및 AI 응답 받기
     */
    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ChatResponsePair> sendMessage(
            @PathVariable Long conversationId,
            @Valid @RequestBody SendChatMessageRequest request) {
        try {
            ChatResponsePair response = chatService.sendMessage(conversationId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 대화의 모든 메시지 조회
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getConversationMessages(
            @PathVariable Long conversationId) {
        try {
            List<ChatMessageResponse> messages = chatService.getConversationMessages(conversationId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자의 모든 대화 조회
     */
    @GetMapping("/users/{userId}/conversations")
    public ResponseEntity<List<ChatConversationResponse>> getUserConversations(
            @PathVariable Long userId) {
        try {
            List<ChatConversationResponse> conversations = chatService.getUserConversations(userId);
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 대화 삭제
     */
    @DeleteMapping("/conversations/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long conversationId) {
        try {
            chatService.deleteConversation(conversationId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 지원되는 시나리오 목록 조회
     */
    @GetMapping("/scenarios")
    public ResponseEntity<List<String>> getSupportedScenarios() {
        List<String> scenarios = List.of("restaurant", "shopping", "direction", "introduction", "daily");
        return ResponseEntity.ok(scenarios);
    }

    /**
     * 지원되는 난이도 목록 조회
     */
    @GetMapping("/difficulties")
    public ResponseEntity<List<String>> getSupportedDifficulties() {
        List<String> difficulties = List.of("beginner", "intermediate", "advanced");
        return ResponseEntity.ok(difficulties);
    }
}