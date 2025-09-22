package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.entity.chat.ChatMessage;
import lk.ijse.skillworker_backend.service.ChatMessageService;
import lk.ijse.skillworker_backend.service.AuthService;
import lk.ijse.skillworker_backend.dto.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

    private final ChatMessageService chatMessageService;
    private final AuthService authService;

    @GetMapping("/contacts/{userId}")
    public ResponseEntity<?> getContacts(@PathVariable String userId) {
        try {
            log.info("Fetching contacts for user: {}", userId);

            List<String> contactIds = chatMessageService.getContactsForUser(userId);

            if (contactIds.isEmpty()) {
                log.info("No chat history found, returning all available users as contacts");
                List<UserResponseDTO> allUsers = authService.getAllUsers();
                contactIds = allUsers.stream()
                        .filter(user -> !user.getId().toString().equals(userId)) // Exclude self
                        .map(user -> user.getId().toString()) // Convert Long to String
                        .toList();
            }

            log.info("Found {} contacts for user {}", contactIds.size(), userId);
            return ResponseEntity.ok(contactIds);

        } catch (Exception e) {
            log.error("Error fetching contacts for user {}: ", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching contacts: " + e.getMessage());
        }
    }

    @GetMapping("/history/{user1}/{user2}")
    public ResponseEntity<?> getChatHistory(@PathVariable String user1, @PathVariable String user2) {
        try {
            log.info("Fetching chat history between {} and {}", user1, user2);
            List<ChatMessage> chatHistory = chatMessageService.getChatHistory(user1, user2);
            log.info("Found {} messages in chat history", chatHistory.size());
            return ResponseEntity.ok(chatHistory);
        } catch (Exception e) {
            log.error("Error fetching chat history: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching chat history: " + e.getMessage());
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userId) {
        try {
            log.info("Fetching user info for: {}", userId);
            return ResponseEntity.ok().body("User info endpoint - to be implemented with proper user service");
        } catch (Exception e) {
            log.error("Error fetching user info: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching user info: " + e.getMessage());
        }
    }

    @GetMapping("/recent/{userId}")
    public ResponseEntity<?> getRecentChats(@PathVariable String userId) {
        try {
            log.info("Fetching recent chats for user: {}", userId);
            List<ChatMessage> recentChats = chatMessageService.getRecentChats(userId);
            return ResponseEntity.ok(recentChats);
        } catch (Exception e) {
            log.error("Error fetching recent chats: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch recent chats");
        }
    }

    @DeleteMapping("/history/{user1}/{user2}")
    public ResponseEntity<?> clearChatHistory(@PathVariable String user1, @PathVariable String user2) {
        try {
            log.info("Clearing chat history between {} and {}", user1, user2);
            chatMessageService.clearChatHistory(user1, user2);
            return ResponseEntity.ok("Chat history cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing chat history: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear chat history");
        }
    }
}
