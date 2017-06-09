package com.messageapp.repository.message;


import com.messageapp.JhipmessageappApp;
import com.messageapp.domain.Message;
import com.messageapp.domain.User;
import com.messageapp.repository.MessageRepository;
import com.messageapp.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipmessageappApp.class)
@Profile("h2")
@Transactional
public class FindMessagesForConversationTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private static Message defaultMessage;
    private static User user1;
    private static User user2;

    @Before
    public void setup() {
        user1 = userRepository.findOne(4L);
        user2 = userRepository.findOne(5L);
        defaultMessage = new Message()
                .messageText("default message text")
                .sender(user1)
                .receiver(user2);
    }

    @Test
    public void shouldBeFirstInList(){
        //given
        messageRepository.save(defaultMessage);

        //when
        List<Message> messages = messageRepository.findMessagesForConversation(user1.getLogin(), user2.getLogin());

        //then
        assertThat(messages.get(0)).isEqualTo(defaultMessage);
    }



    @Test
    public void testMessageInConversation(){
        //given
        messageRepository.save(defaultMessage);

        //when
        List<Message> messagesInReceiverConversation = messageRepository.findMessagesForConversation(user1.getLogin(), user2.getLogin());
        List<Message> messagesInSenderConversation = messageRepository.findMessagesForConversation(user2.getLogin(), user1.getLogin());

        //then
        assertThat(messagesInSenderConversation.get(0)).isEqualTo(defaultMessage);
        assertThat(messagesInReceiverConversation.get(0)).isEqualTo(defaultMessage);
    }

    @Test
    public void testMessageBeyondConversation(){
        //given
        defaultMessage.setReceiver(userRepository.findOne(1L));
        defaultMessage.setMessageText("New Message Text");
        messageRepository.save(defaultMessage);

        //when
        List<Message> messagesInReceiverConversation = messageRepository.findMessagesForConversation(user1.getLogin(), user2.getLogin());
        List<Message> messagesInSenderConversation = messageRepository.findMessagesForConversation(user2.getLogin(), user1.getLogin());

        //then
        assertThat(messagesInSenderConversation).doesNotContain(defaultMessage);
        assertThat(messagesInReceiverConversation).doesNotContain(defaultMessage);
    }










}
