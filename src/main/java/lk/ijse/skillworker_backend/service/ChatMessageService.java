package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.entity.chat.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage message);
    List<ChatMessage> getChatHistory(String user1, String user2);
    List<String> getContactsForUser(String userId);
    List<ChatMessage> getRecentChats(String userId);
    void clearChatHistory(String user1, String user2);
    List<ChatMessage> getMessagesByReceiver(String receiverId);
    List<ChatMessage> getMessagesBySender(String senderId);
}
