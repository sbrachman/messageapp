package com.messageapp.service;


import com.messageapp.domain.Message;
import com.messageapp.domain.User;
import com.messageapp.repository.MessageRepository;
import com.messageapp.service.dto.LatestUserMessage;
import com.messageapp.service.dto.MessageCreateDTO;
import com.messageapp.service.dto.MessageQueryDTO;
import com.messageapp.service.mapper.MessageMapper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;


    public MessageQueryDTO addMessage(MessageCreateDTO messageCreateDTO) {
        Message newMessage = messageMapper.toMessage(messageCreateDTO);
        Message returnedMessage = messageRepository.save(newMessage);
        MessageQueryDTO addedMessage = messageMapper.toMessageQueryDTO(returnedMessage);
        return addedMessage;
    }


    @Transactional
    public List<MessageQueryDTO> getMessagesForConversation(String interlocutorLogin){
        getUndeliveredAndSetAsDelivered(interlocutorLogin);
        List<Message> messages = messageRepository.findMessagesForConversation(interlocutorLogin);
        Collections.reverse(messages);
        return mapMessagesListToDTO(messages);
    }

    @Transactional
    public Optional<List<MessageQueryDTO>> checkForNewMessages(String interlocutorLogin) {
        List<Message> undelivered = getUndeliveredAndSetAsDelivered(interlocutorLogin);
        if(!undelivered.isEmpty()){
            return Optional.of(mapMessagesListToDTO(undelivered));
        } else {
            return Optional.empty();
        }
    }

    public List<LatestUserMessage> getFriendsWithLastMessage(Long loggedUserId) {
        List<LatestUserMessage> latestUserMessages
                = messageRepository.getLastInterlocutorsWithMsg(loggedUserId);
        return latestUserMessages;
    }

    @Transactional
    private List<Message> getUndeliveredAndSetAsDelivered(String interlocutorLogin){
        List<Message> undelivered = messageRepository.getUndelivered(interlocutorLogin);
        undelivered.forEach(message -> message.setDelivered(true));
        messageRepository.save(undelivered);
        return undelivered;
    }

    private List<MessageQueryDTO> mapMessagesListToDTO(List<Message> messages) {
        List<MessageQueryDTO> messagesDTOs = messages
                .stream()
                .map(messageMapper::toMessageQueryDTO)
                .collect(toList());
        return messagesDTOs;
    }
}
