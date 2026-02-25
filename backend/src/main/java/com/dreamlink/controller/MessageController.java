package com.dreamlink.controller;

import com.dreamlink.dto.ApiResponse;
import com.dreamlink.dto.message.SendMessageRequest;
import com.dreamlink.entity.Message;
import com.dreamlink.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/user/{userId}")
    public ApiResponse<?> getUserMessages(@PathVariable Long userId) {
        try {
            List<Message> messages = messageService.getUserMessages(userId);
            return ApiResponse.success(messages);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/chat/{userId1}/{userId2}")
    public ApiResponse<?> getChatHistory(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            List<Message> messages = messageService.getChatHistory(userId1, userId2);
            return ApiResponse.success(messages);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<?> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            Message message = messageService.sendMessage(request);
            return ApiResponse.success(message);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/read/{messageId}")
    public ApiResponse<?> markAsRead(@PathVariable Long messageId) {
        try {
            Message message = messageService.markAsRead(messageId);
            return ApiResponse.success(message);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

}
