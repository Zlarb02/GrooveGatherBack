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

    // Enregistre un message dans la base de données
    public MessageEntity saveMessage(MessageEntity message) {
        return messageRepo.save(message);
    }

    // Met à jour un message dans la base de données
    public MessageEntity updateMessage(MessageEntity message) {
        if (messageRepo.existsById(message.getId())) {
            return messageRepo.save(message);
        } else {
            throw new RuntimeException("Message not found");
        }
    }

    public MessageResponseDto sendMessage(UserEntity sender, UserEntity receiver, String content,
            MessageEntity replyToMessage) {
        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setReplyToMessage(replyToMessage);
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

    public MessageEntity getMessageById(Long id) {
        return messageRepo.findById(id).orElseThrow();
    }

    private MessageResponseDto convertToResponseDTO(MessageEntity message) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setSenderName(message.getSender().getName());
        dto.setReceiverName(message.getReceiver() != null ? message.getReceiver().getName() : null);
        dto.setSenderPicture(message.getSender().getPicture());
        dto.setReceiverPicture(message.getReceiver() != null ? message.getReceiver().getPicture() : null);
        if (message.getReplyToMessage() != null) {
            dto.setReplyToMessageId(message.getReplyToMessage().getId());
            dto.setReplyToMessageContent(message.getReplyToMessage().getContent());
        }
        return dto;
    }
}
