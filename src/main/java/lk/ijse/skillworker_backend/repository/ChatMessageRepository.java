package lk.ijse.skillworker_backend.repository;


import lk.ijse.skillworker_backend.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Find chat history between two users
    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.timestamp ASC")
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(
            @Param("user1") String user1,
            @Param("user2") String user2);

    // Find all messages involving a specific user (for contacts)
    @Query("SELECT m FROM ChatMessage m WHERE m.sender = :userId OR m.receiver = :userId")
    List<ChatMessage> findMessagesByUser(@Param("userId") String userId);

    // Find recent chats for a user (latest message from each conversation)
    @Query("SELECT m FROM ChatMessage m WHERE m.id IN (" +
           "SELECT MAX(m2.id) FROM ChatMessage m2 WHERE " +
           "(m2.sender = :userId OR m2.receiver = :userId) " +
           "GROUP BY CASE WHEN m2.sender = :userId THEN m2.receiver ELSE m2.sender END" +
           ") ORDER BY m.timestamp DESC")
    List<ChatMessage> findRecentChatsByUser(@Param("userId") String userId);

    // Find messages by receiver
    List<ChatMessage> findByReceiver(String receiver);

    // Find messages by sender
    List<ChatMessage> findBySender(String sender);

    // Delete chat history between two users
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1)")
    int deleteBySenderAndReceiverOrReceiverAndSender(@Param("user1") String user1, @Param("user2") String user2);

    // Find messages by timestamp range
    @Query("SELECT m FROM ChatMessage m WHERE m.timestamp BETWEEN :startTime AND :endTime ORDER BY m.timestamp ASC")
    List<ChatMessage> findByTimestampBetween(@Param("startTime") String startTime, @Param("endTime") String endTime);

    // Count unread messages for a user
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.receiver = :userId AND m.timestamp > :lastSeenTime")
    long countUnreadMessages(@Param("userId") String userId, @Param("lastSeenTime") String lastSeenTime);
}
