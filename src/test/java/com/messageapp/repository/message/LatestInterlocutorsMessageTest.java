package com.messageapp.repository.message;

import com.messageapp.JhipmessageappApp;
import com.messageapp.domain.Message;
import com.messageapp.repository.MessageRepository;
import com.messageapp.repository.UserRepository;
import com.messageapp.service.dto.LatestUserMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipmessageappApp.class)
@Profile("h2")
@Transactional
public class LatestInterlocutorsMessageTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private static Message defaultMessage;


    @Before
    public void setup() {
         defaultMessage = new Message()
                .messageText("default message text")
                .sender(userRepository.findOne(1L))
                .receiver(userRepository.findOne(2L));
    }

    @Test
    public void returnObjectList(){
        //when
        List<LatestUserMessage> latest = messageRepository.getLastInterlocutorsWithMsg(1L);

        //then
        assertThat(latest).isNotNull();
    }

    @Test
    public void returnLatestMessage(){
        //given
        messageRepository.save(defaultMessage);

        //when
        List<LatestUserMessage> listForUser1 = messageRepository.getLastInterlocutorsWithMsg(1L);
        LatestUserMessage returned = listForUser1.get(0);

        //then
        assertThat(defaultMessage.getMessageText()).isEqualTo(returned.getMessage());
        assertThat(defaultMessage.getSentTime()).isEqualTo(returned.getMessageTime());
        assertThat(defaultMessage.getSender().getId()).isEqualTo(returned.getLastMessageSenderId());
    }

    @Test
    public void shouldReturnUndeliveredMessage(){
        //given
        messageRepository.save(defaultMessage);

        //when
        List<LatestUserMessage> listForUser1 = messageRepository.getLastInterlocutorsWithMsg(1L);

        //then
        assertThat(listForUser1.get(0).getMessageDelivered()).isFalse();
    }

    @Test
    public void returnLastInterlocutor(){
        //given
        Message latestMessage = new Message()
                .messageText("text")
                .sender(userRepository.findOne(2L))
                .receiver(userRepository.findOne(4L));
        messageRepository.save(latestMessage);

        //when
        List<LatestUserMessage> listForUser2 = messageRepository.getLastInterlocutorsWithMsg(2L);
        List<LatestUserMessage> listForUser4 = messageRepository.getLastInterlocutorsWithMsg(4L);
        String lastInterlocutorForUser2 = listForUser2.get(0).getLogin();
        String lastInterlocutorForUser4 = listForUser4.get(0).getLogin();

        //then
        assertThat(lastInterlocutorForUser2).isEqualTo(userRepository.findOne(4L).getLogin());
        assertThat(lastInterlocutorForUser4).isEqualTo(userRepository.findOne(2L).getLogin());
    }


    @Test
    public void checkForDuplicatingInterlocutors(){
        List<LatestUserMessage> listForUser1 = messageRepository.getLastInterlocutorsWithMsg(1L);
        List<String> contactLoginList = new ArrayList<>();
        for (LatestUserMessage l: listForUser1){
            contactLoginList.add(l.getLogin());
        }


        //then
        assertThat(contactLoginList).doesNotHaveDuplicates();
    }










}




