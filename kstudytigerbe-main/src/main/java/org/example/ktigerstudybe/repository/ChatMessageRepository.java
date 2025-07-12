package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversation_ConversationIdOrderByTimestamp(Long conversationId);

    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.conversationId = :conversationId ORDER BY m.timestamp ASC")
    List<ChatMessage> findMessagesInConversation(@Param("conversationId") Long conversationId);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.conversation.conversationId = :conversationId AND m.messageType = :messageType")
    int countByConversationIdAndMessageType(@Param("conversationId") Long conversationId, @Param("messageType") String messageType);
}