package com.messageapp.service.dto;

import java.time.Instant;

public interface LatestUserMessage {

    String getDelivered();

    String getLogin();

    String getMessage();

    Instant getMessagetime();

}
