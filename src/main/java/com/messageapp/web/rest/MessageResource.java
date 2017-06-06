package com.messageapp.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.messageapp.config.Constants;
import com.messageapp.domain.User;
import com.messageapp.repository.MessageRepository;
import com.messageapp.repository.UserRepository;
import com.messageapp.security.AuthoritiesConstants;
import com.messageapp.security.SecurityUtils;
import com.messageapp.service.MessageService;
import com.messageapp.service.dto.LatestUserMessage;
import com.messageapp.service.dto.MessageCreateDTO;
import com.messageapp.service.dto.MessageQueryDTO;
import com.messageapp.service.mapper.MessageMapper;
import com.messageapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;


/**
 * REST controller for managing Message.
 */
@EnableAsync
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    public MessageResource(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }


    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/messages")
    @Timed
    public ResponseEntity<List<LatestUserMessage>> getFriendsWithLastMessage() {
        log.debug("REST request to get a Last Interlocutors with last message in particular conversation");
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Long currentUserId = userRepository.findOneByLogin(currentUserLogin).get().getId();
        return ResponseEntity.ok(messageService.getFriendsWithLastMessage(currentUserId));
    }


    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/messages/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity getMessagesForConversation(@PathVariable String login){
        Optional<User> interlocutor = userRepository.findOneByLogin(login.toLowerCase());
        if (!interlocutor.isPresent()){
            return ResponseEntity.badRequest().body(null);
        } else{
            List<MessageQueryDTO> messagesForConversation = messageService.getMessagesForConversation(interlocutor.get().getLogin());
            return ResponseEntity.ok(messagesForConversation);
        }
    }

    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/messages/{login:" + Constants.LOGIN_REGEX + "}/undelivered")
    public ResponseEntity checkForNewMessages(@PathVariable String login)  {
        Optional<User> interlocutor = userRepository.findOneByLogin(login);
        if (!interlocutor.isPresent()){
            return ResponseEntity.badRequest().body(null);
        }
        Optional<List<MessageQueryDTO>> undeliveredMessages = messageService.checkForNewMessages(login);
        if (undeliveredMessages.isPresent()){
            return ResponseEntity.ok(undeliveredMessages.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @Secured(AuthoritiesConstants.USER)
    @PostMapping("/messages/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity createMessage(@RequestBody @Valid MessageCreateDTO messageCreateDTO,
                                        @PathVariable String login) throws URISyntaxException{
        log.debug("REST request to save Message : {}", messageCreateDTO);
        messageCreateDTO.setReceiverLogin(login);
        messageCreateDTO.setSenderLogin(SecurityUtils.getCurrentUserLogin());

        if (messageCreateDTO.getSenderLogin().equals(login)){
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "wronguser", "You can not send messages to yourself"))
                    .body(null);
        }
        else if (!userRepository.findOneByLogin(login).isPresent()){
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "usernotexists", "Receiver of this message does not exist"))
                    .body(null);
        } else {
            MessageQueryDTO createdMessage = messageService.addMessage(messageCreateDTO);
            return ResponseEntity.ok(createdMessage);
        }
    }


}
