package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.ChatMessageDTO;
import lk.ijse.skillworker_backend.entity.chat.ChatMessage;
import lk.ijse.skillworker_backend.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessage) {
        try {
            chatMessage.setTimestamp(String.valueOf(System.currentTimeMillis()));
            log.info("Broadcasting public message from: {}", chatMessage.getSender());
            return chatMessage;
        } catch (Exception e) {
            log.error("Error sending public message: ", e);
            throw new RuntimeException("Failed to send message");
        }
    }

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        try {
            log.info("Sending private message from {} to {}", chatMessageDTO.getSender(), chatMessageDTO.getReceiver());

            // Set timestamp
            chatMessageDTO.setTimestamp(String.valueOf(System.currentTimeMillis()));

            // Save message to database
            ChatMessage chatMessage = ChatMessage.builder()
                    .sender(chatMessageDTO.getSender())
                    .receiver(chatMessageDTO.getReceiver())
                    .content(chatMessageDTO.getContent())
                    .timestamp(chatMessageDTO.getTimestamp())
                    .build();

            ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
            log.info("Message saved to database with ID: {}", savedMessage.getId());

            // Update DTO with saved message ID
            chatMessageDTO.setTimestamp(savedMessage.getTimestamp());

            // Send to receiver
            messagingTemplate.convertAndSendToUser(
                    chatMessageDTO.getReceiver(),
                    "/queue/messages",
                    chatMessageDTO
            );

            // Send confirmation back to sender
            messagingTemplate.convertAndSendToUser(
                    chatMessageDTO.getSender(),
                    "/queue/messages",
                    chatMessageDTO
            );

            log.info("Private message sent successfully");

        } catch (Exception e) {
            log.error("Error sending private message: ", e);

            // Send error notification to sender
            ChatMessageDTO errorMessage = new ChatMessageDTO();
            errorMessage.setSender("system");
            errorMessage.setReceiver(chatMessageDTO.getSender());
            errorMessage.setContent("Failed to send message. Please try again.");
            errorMessage.setTimestamp(String.valueOf(System.currentTimeMillis()));

            messagingTemplate.convertAndSendToUser(
                    chatMessageDTO.getSender(),
                    "/queue/errors",
                    errorMessage
            );
        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Add username in web socket session
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            log.info("User {} joined the chat", chatMessage.getSender());
            return chatMessage;
        } catch (Exception e) {
            log.error("Error adding user to chat: ", e);
            throw new RuntimeException("Failed to add user to chat");
        }
    }
}
