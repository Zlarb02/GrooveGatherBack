package com.groovegather.back.dtos.message;

public class MessageRequestDto {
    private String receiverName;
    private String content;
    private Long replyToMessageId; // Ajouté pour la réponse

    // Getters et setters
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(Long replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }
}
