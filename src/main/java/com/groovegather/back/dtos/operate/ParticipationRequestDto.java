package com.groovegather.back.dtos.operate;

public class ParticipationRequestDto {
    private Long messageId;
    private boolean accepted;

    // Getters et setters
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
