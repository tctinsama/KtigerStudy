package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {

    List<ChatConversation> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT c FROM ChatConversation c WHERE c.user.userId = :userId AND c.scenario = :scenario ORDER BY c.createdAt DESC")
    List<ChatConversation> findByUserIdAndScenario(@Param("userId") Long userId, @Param("scenario") String scenario);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.conversation.conversationId = :conversationId")
    int countMessagesByConversationId(@Param("conversationId") Long conversationId);
}