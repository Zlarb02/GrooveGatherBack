package com.groovegather.back.dtos.message;

import java.time.LocalDateTime;

public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private String senderName;
    private String receiverName;
    private String senderPicture;
    private String receiverPicture;
    private Long replyToMessageId; // Ajouté pour la réponse
    private String replyToMessageContent; // Contenu du message auquel il répond
    private Long projectId; // Ajouté pour stocker l'ID du projet

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderPicture() {
        return senderPicture;
    }

    public void setSenderPicture(String senderPicture) {
        this.senderPicture = senderPicture;
    }

    public String getReceiverPicture() {
        return receiverPicture;
    }

    public void setReceiverPicture(String receiverPicture) {
        this.receiverPicture = receiverPicture;
    }

    public Long getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(Long replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }

    public String getReplyToMessageContent() {
        return replyToMessageContent;
    }

    public void setReplyToMessageContent(String replyToMessageContent) {
        this.replyToMessageContent = replyToMessageContent;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
