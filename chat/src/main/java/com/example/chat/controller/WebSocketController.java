package com.example.chat.controller;

import com.example.chat.persistance.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println(chatMessage);
        messagingTemplate.convertAndSend("/msg/" + chatMessage.getReceiver(), chatMessage);
        messagingTemplate.convertAndSend("/msg/" + chatMessage.getSender(), chatMessage);
    }

    @MessageMapping("/typing")
    public void notifyTyping(@Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/notify/typing/" + chatMessage.getReceiver(), chatMessage);
    }

    @MessageMapping("/seen")
    public void notifySeen(@Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/notify/seen/" + chatMessage.getReceiver(), chatMessage);
    }
}
