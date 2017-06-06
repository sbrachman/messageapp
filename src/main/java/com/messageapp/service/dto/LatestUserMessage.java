package com.messageapp.service.dto;

import java.time.Instant;

public interface LatestUserMessage {

    boolean getMessageDelivered();

    String getLogin();

    String getMessage();

    Long getLastMessageSenderId();

    Instant getMessageTime();

}
