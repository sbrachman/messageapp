package com.messageapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class MessageCreateDTO {

    @NotNull
    @Size(min = 1, max = 1000)
    private String messageText;

    private String senderLogin;

    private String receiverLogin;

    public MessageCreateDTO() {
    }

    public MessageCreateDTO(String messageText, String senderLogin, String receiverLogin) {
        this.messageText = messageText;
        this.senderLogin = senderLogin;
        this.receiverLogin = receiverLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public void setReceiverLogin(String receiverLogin) {
        this.receiverLogin = receiverLogin;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public String getReceiverLogin() {
        return receiverLogin;
    }
}
