package com.messageapp.service;


import com.messageapp.JhipmessageappApp;
import com.messageapp.repository.UserRepository;
import com.messageapp.service.dto.MessageCreateDTO;
import com.messageapp.service.dto.MessageQueryDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipmessageappApp.class)
@Transactional
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    private static MessageCreateDTO defaultMessage;
    private static MessageCreateDTO anotherMessage;
    private static String USER1;
    private static String USER2;

    @Before
    public void setup(){
        USER1 = userRepository.findOne(1L).getLogin();
        USER2 = userRepository.findOne(2L).getLogin();
        defaultMessage = new MessageCreateDTO("MessageService Test", USER1, USER2);
        anotherMessage = new MessageCreateDTO("Another Message", USER1, USER2);
    }

    @Test
    public void testAddMessage(){
        //when
        MessageQueryDTO returnedMessage = messageService.addMessage(defaultMessage);

        //then
        assertThat(defaultMessage.getMessageText().equals(returnedMessage.getMessageText()));
        assertThat(defaultMessage.getReceiverLogin().equals(returnedMessage.getReceiverLogin()));
        assertThat(defaultMessage.getSenderLogin().equals(returnedMessage.getSenderLogin()));
        assertThat(returnedMessage.isDelivered()).isFalse();
    }

    @Test
    public void testOrderingMessages(){
        //given
        MessageQueryDTO returnedMessage = messageService.addMessage(defaultMessage);
        MessageQueryDTO returnedMessage2 = messageService.addMessage(anotherMessage);

        //when
        List<MessageQueryDTO> returnedList = messageService.getMessagesForConversation(USER1, USER2);

        //then
        assertThat((returnedList.get(0).getMessageText()).equals(anotherMessage.getMessageText()));
        assertThat((returnedList.get(1).getMessageText()).equals(defaultMessage.getMessageText()));
    }

    @Test
    public void shouldReturnList(){
        //given
        messageService.addMessage(defaultMessage);

        //when
        Optional<List<MessageQueryDTO>> messages = messageService.checkForNewMessages(USER1, USER2);

        assertThat(messages.isPresent());
    }

    @Test
    public void shouldReturnUndeliveredForReceiver(){
        //given
        messageService.addMessage(defaultMessage);

        //when
        List<MessageQueryDTO> messages = messageService.checkForNewMessages(USER1, USER2).get();
        MessageQueryDTO returnedMessage = messages.get(messages.size()-1);

        //then
        assertThat(returnedMessage.getMessageText()).isEqualTo(defaultMessage.getMessageText());
        assertThat(returnedMessage.getSenderLogin()).isEqualTo(defaultMessage.getSenderLogin());
        assertThat(returnedMessage.getReceiverLogin()).isEqualTo(defaultMessage.getReceiverLogin());
    }

    @Test
    public void shouldntReturnUndeliveredForSender(){
        //given
        messageService.addMessage(defaultMessage);

        //when
        Optional<List<MessageQueryDTO>> messages = messageService.checkForNewMessages(USER2, USER1);

        //then
        assertThat(messages).isEmpty();
    }




}
