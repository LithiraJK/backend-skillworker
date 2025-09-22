package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.entity.chat.ChatMessage;
import lk.ijse.skillworker_backend.repository.ChatMessageRepository;
import lk.ijse.skillworker_backend.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage saveMessage(ChatMessage message) {
        try {
            message.setTimestamp(String.valueOf(System.currentTimeMillis()));
            ChatMessage savedMessage = chatMessageRepository.save(message);
            log.info("Message saved successfully with ID: {}", savedMessage.getId());
            return savedMessage;
        } catch (Exception e) {
            log.error("Error saving message: ", e);
            throw new RuntimeException("Failed to save message to database", e);
        }
    }

    @Override
    public List<ChatMessage> getChatHistory(String user1, String user2) {
        try {
            List<ChatMessage> messages = chatMessageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(user1, user2);
            log.info("Retrieved {} messages between {} and {}", messages.size(), user1, user2);
            return messages;
        } catch (Exception e) {
            log.error("Error retrieving chat history between {} and {}: ", user1, user2, e);
            throw new RuntimeException("Failed to retrieve chat history", e);
        }
    }

    @Override
    public List<String> getContactsForUser(String userId) {
        try {
            List<ChatMessage> userMessages = chatMessageRepository.findMessagesByUser(userId);

            // Extract unique contact IDs (both senders and receivers, excluding the user)
            List<String> contacts = userMessages.stream()
                    .flatMap(msg -> List.of(msg.getSender(), msg.getReceiver()).stream())
                    .filter(id -> !id.equals(userId))
                    .distinct()
                    .collect(Collectors.toList());
            log.info("Found {} contacts for user {}", contacts.size(), userId);
            return contacts;
        } catch (Exception e) {
            log.error("Error retrieving contacts for user {}: ", userId, e);
            throw new RuntimeException("Failed to retrieve user contacts", e);
        }
    }

    @Override
    public List<ChatMessage> getRecentChats(String userId) {
        try {
            List<ChatMessage> recentMessages = chatMessageRepository.findRecentChatsByUser(userId);
            log.info("Found {} recent chats for user {}", recentMessages.size(), userId);
            return recentMessages;
        } catch (Exception e) {
            log.error("Error retrieving recent chats for user {}: ", userId, e);
            throw new RuntimeException("Failed to retrieve recent chats", e);
        }
    }

    @Override
    public void clearChatHistory(String user1, String user2) {
        try {
            int deletedCount = chatMessageRepository.deleteBySenderAndReceiverOrReceiverAndSender(user1, user2);
            log.info("Cleared {} messages between {} and {}", deletedCount, user1, user2);
        } catch (Exception e) {
            log.error("Error clearing chat history between {} and {}: ", user1, user2, e);
            throw new RuntimeException("Failed to clear chat history", e);
        }
    }

    @Override
    public List<ChatMessage> getMessagesByReceiver(String receiverId) {
        try {
            return chatMessageRepository.findByReceiver(receiverId);
        } catch (Exception e) {
            log.error("Error fetching messages by receiver: ", e);
            throw new RuntimeException("Failed to fetch messages", e);
        }
    }

    @Override
    public List<ChatMessage> getMessagesBySender(String senderId) {
        try {
            return chatMessageRepository.findBySender(senderId);
        } catch (Exception e) {
            log.error("Error fetching messages by sender: ", e);
            throw new RuntimeException("Failed to fetch messages", e);
        }
    }
}
