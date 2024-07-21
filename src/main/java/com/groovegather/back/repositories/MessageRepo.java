package com.groovegather.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.MessageEntity;

public interface MessageRepo extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderId(Long senderId);

    List<MessageEntity> findByReceiverId(Long receiverId);
}
