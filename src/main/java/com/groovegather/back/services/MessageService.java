package com.groovegather.back.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.message.MessageResponseDto;
import com.groovegather.back.entities.MessageEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.repositories.MessageRepo;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public MessageResponseDto sendMessage(UserEntity sender, UserEntity receiver, String content) {
        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        MessageEntity savedMessage = messageRepo.save(message);

        return convertToResponseDTO(savedMessage);
    }

    public List<MessageResponseDto> getMessagesBySender(UserEntity sender) {
        return messageRepo.findBySenderId(sender.getId())
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MessageResponseDto> getMessagesByReceiver(UserEntity receiver) {
        return messageRepo.findByReceiverId(receiver.getId())
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private MessageResponseDto convertToResponseDTO(MessageEntity message) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setSenderEmail(message.getSender().getEmail());
        dto.setReceiverEmail(message.getReceiver() != null ? message.getReceiver().getEmail() : null);
        return dto;
    }
}
