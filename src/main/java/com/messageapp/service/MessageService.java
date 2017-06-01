package com.messageapp.service;


import com.messageapp.domain.Message;
import com.messageapp.repository.MessageRepository;
import com.messageapp.service.dto.MessageCreateDTO;
import com.messageapp.service.dto.MessageQueryDTO;
import com.messageapp.service.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    //UNUSED
    public Message getLastestMessageForConversation(Long userId, Long interlocutorId){
        return messageRepository.findLatestMessageForConversation(userId, interlocutorId);
    }

    public List<MessageQueryDTO> getMessagesForConversation(String interlocutorLogin){
        List<Message> messages = messageRepository.findMessagesForConversation(interlocutorLogin);
        List<MessageQueryDTO> messagesDTOs = messages
                .stream()
                .map(messageMapper::toMessageQueryDTO)
                .collect(toList());
        Collections.reverse(messagesDTOs);
        return messagesDTOs;
    }


}
