package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messages;

    public MessageService(MessageRepository messages) {
        this.messages = messages;
    }

    public Message create(Message newMsg) {
        return messages.save(newMsg);
    }

    public Message get(int id) {
        return messages.findById(id).orElse(null);
    }

    public List<Message> get() {
        return messages.findAll();
    }

    public List<Message> getByUser(int userId) {
        return messages.findByPostedBy(userId);
    }

    public void delete(int id) {
        messages.deleteById(id);
    }

    public Message update(int id, String newContent) {
        var message = messages.getById(id);
        message.setMessageText(newContent);
        messages.save(message);
        return message;
    }
}
