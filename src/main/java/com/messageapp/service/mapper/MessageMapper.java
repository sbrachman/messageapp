package com.messageapp.service.mapper;


import com.messageapp.domain.Message;
import com.messageapp.repository.MessageRepository;
import com.messageapp.service.dto.MessageCreateDTO;
import com.messageapp.service.dto.MessageQueryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class MessageMapper {

    @Autowired
    private MessageRepository messageRepository;


    @Mapping(target = "senderLogin", source = "sender.login")
    @Mapping(target = "receiverLogin", source = "receiver.login")
    public abstract MessageQueryDTO toMessageQueryDTO(Message message);

    @Mapping(target = "sender", source = "senderLogin")
    @Mapping(target = "receiver", source = "receiverLogin")
    public abstract Message toMessage(MessageCreateDTO messageCreateDTO);


}
