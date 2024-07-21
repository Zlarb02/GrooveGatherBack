package com.groovegather.back.dtos.message;

public class MessageRequestDto {
    private String receiverEmail;
    private String content;

    // Getters and setters
    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
