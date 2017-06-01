package com.messageapp.service.dto;

import java.time.Instant;

public class LastestUserMessageDTO {

    private String InterlocutorLogin;
    private String MessageText;
    private Instant sentTime;

    public String getInterlocutorLogin() {
        return InterlocutorLogin;
    }

    public void setInterlocutorLogin(String interlocutorLogin) {
        InterlocutorLogin = interlocutorLogin;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public Instant getSentTime() {
        return sentTime;
    }

    public void setSentTime(Instant sentTime) {
        this.sentTime = sentTime;
    }

    @Override
    public String toString() {
        return "LastestUserMessageDTO{" +
            "InterlocutorLogin='" + InterlocutorLogin + '\'' +
            ", MessageText='" + MessageText + '\'' +
            ", sentTime=" + sentTime +
            '}';
    }
}
