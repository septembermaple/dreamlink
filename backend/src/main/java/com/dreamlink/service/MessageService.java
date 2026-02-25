package com.dreamlink.service;

import com.dreamlink.dto.message.SendMessageRequest;
import com.dreamlink.entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> getUserMessages(Long userId);

    List<Message> getChatHistory(Long userId1, Long userId2);

    Message sendMessage(SendMessageRequest request);

    Message markAsRead(Long messageId);

}
