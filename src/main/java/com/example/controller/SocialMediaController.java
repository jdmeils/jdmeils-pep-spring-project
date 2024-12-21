package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accounts;
    private final MessageService messages;

    public SocialMediaController(AccountService accounts, MessageService messages) {
        this.accounts = accounts;
        this.messages = messages;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account newUser) {
        if (
            newUser.getUsername().isBlank()
            || newUser.getPassword().length() < 4
        ) {
            return ResponseEntity.badRequest().build();
        }
        if (accounts.get(newUser.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var user = accounts.create(newUser);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        var existing = accounts.get(account.getUsername());
        if (
            existing != null
            && existing.getPassword().equals(account.getPassword())
        ) {
            return ResponseEntity.ok(existing);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        var user = accounts.get(message.getPostedBy());
        if (
            message.getMessageText().isBlank()
            || message.getMessageText().length() > 255
            || user == null
        ) {
            return ResponseEntity.badRequest().build();
        }

        var newMsg = messages.create(message);
        return ResponseEntity.ok(newMsg);
    }

    @GetMapping("/messages") 
    public List<Message> getAllMessages() {
        return messages.get();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable int accountId) {
        return messages.getByUser(accountId);
    }

    @GetMapping("/messages/{id}")
    public Message getMessageById(@PathVariable int id) {
        return messages.get(id);
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int id) {
        var deleting = messages.get(id);
        if(deleting != null) {
            messages.delete(id);
            return ResponseEntity.ok(1);
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable int id, @RequestBody Message message) {
        var newContent = message.getMessageText();
        var existing = messages.get(id);
        if (
            existing == null
            || newContent.isBlank()
            || newContent.length() > 255
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        messages.update(id, newContent);
        return ResponseEntity.ok(1);
    }
}
