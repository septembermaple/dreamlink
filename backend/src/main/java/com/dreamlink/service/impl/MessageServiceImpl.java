package com.dreamlink.service.impl;

import com.dreamlink.dto.message.SendMessageRequest;
import com.dreamlink.entity.Message;
import com.dreamlink.repository.MessageRepository;
import com.dreamlink.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getUserMessages(Long userId) {
        return messageRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Message> getChatHistory(Long userId1, Long userId2) {
        return messageRepository.findByUserIdAndOtherUserIdOrUserIdAndOtherUserIdOrderByCreatedAtAsc(
                userId1, userId2, userId2, userId1
        );
    }

    @Override
    public Message sendMessage(SendMessageRequest request) {
        Message message = new Message();
        message.setType("chat");
        message.setContent(request.getContent());
        message.setUserId(request.getUserId());
        message.setOtherUserId(request.getOtherUserId());
        message.setRead(false);

        return messageRepository.save(message);
    }

    @Override
    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));

        message.setRead(true);
        return messageRepository.save(message);
    }

}
