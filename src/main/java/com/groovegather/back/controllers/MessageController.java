package com.groovegather.back.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.dtos.message.MessageRequestDto;
import com.groovegather.back.dtos.message.MessageResponseDto;
import com.groovegather.back.entities.MessageEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.MessageService;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepo userRepository;

    @PostMapping("/send")
    public MessageResponseDto sendMessage(@AuthenticationPrincipal UserEntity sender,
            @RequestBody MessageRequestDto request) {
        UserEntity receiver = userRepository.findByName(request.getReceiverName()).orElseThrow();
        MessageEntity replyToMessage = null;
        if (request.getReplyToMessageId() != null) {
            replyToMessage = messageService.getMessageById(request.getReplyToMessageId());
        }
        return messageService.sendMessage(sender, receiver, request.getContent(), replyToMessage);
    }

    @GetMapping("/sent")
    public List<MessageResponseDto> getSentMessages(@AuthenticationPrincipal UserEntity sender) {
        return messageService.getMessagesBySender(sender);
    }

    @GetMapping("/received")
    public List<MessageResponseDto> getReceivedMessages(@AuthenticationPrincipal UserEntity receiver) {
        return messageService.getMessagesByReceiver(receiver);
    }
}
