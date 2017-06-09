package com.messageapp.service;

import com.messageapp.domain.Message;
import com.messageapp.repository.MessageRepository;
import com.messageapp.service.dto.LatestUserMessage;
import com.messageapp.service.dto.MessageCreateDTO;
import com.messageapp.service.dto.MessageQueryDTO;
import com.messageapp.service.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


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
    public List<MessageQueryDTO> getMessagesForConversation(String interlocutorLogin, String currentUserLogin){
        getUndeliveredAndSetAsDelivered(interlocutorLogin, currentUserLogin);
        List<Message> messages = messageRepository.findMessagesForConversation(interlocutorLogin, currentUserLogin);
        return mapMessagesListToDTO(messages);
    }

    @Transactional
    public Optional<List<MessageQueryDTO>> checkForNewMessages(String interlocutorLogin, String currentUserLogin) {
        List<Message> undelivered = getUndeliveredAndSetAsDelivered(interlocutorLogin, currentUserLogin);
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
    private List<Message> getUndeliveredAndSetAsDelivered(String interlocutorLogin, String currentUserLogin){
        List<Message> undelivered = messageRepository.getUndelivered(interlocutorLogin, currentUserLogin);
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
