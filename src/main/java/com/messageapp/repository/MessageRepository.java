package com.messageapp.repository;

import com.messageapp.domain.Message;
import com.messageapp.domain.User;
import com.messageapp.service.dto.LatestUserMessage;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {


    @Query("select message from Message message where message.sender.login = ?#{principal.username}")
    List<Message> findBySenderIsCurrentUser();

    @Query("select message from Message message where message.receiver.login = ?#{principal.username}")
    List<Message> findByReceiverIsCurrentUser();


    @Query("select m from Message m where" +
            " m.receiver.login = :currentUserLogin and m.sender.login = :interlocutorLogin" +
            " or m.receiver.login = :interlocutorLogin and m.sender.login = :currentUserLogin" +
            " order by m.sentTime desc")
    List<Message> findMessagesForConversation(@Param("interlocutorLogin") String interlocutorLogin, @Param("currentUserLogin") String currentUserLogin);


    @Query("select m from Message m " +
            "where m.receiver.login = :currentUserLogin " +
            "and m.sender.login = :interlocutorLogin " +
            "and m.delivered = false " +
            "order by m.sentTime asc")
    List<Message> getUndelivered(@Param("interlocutorLogin") String interlocutorLogin, @Param("currentUserLogin") String currentUserLogin);


    @Query(value =
        "SELECT m.sender_id as lastMessageSenderId, u.login as login, m.message_text as message, m.delivered as messageDelivered, f.lastmsgtime as messageTime FROM" +
            " (SELECT r.interlocutor_id as interlocutorid, max(r.lastmsgtime) as lastmsgtime" +
            " FROM" +
            " ((SELECT message.sender_id as interlocutor_id, max(message.sent_time) as lastmsgtime, message.message_text as text" +
            " FROM message" +
            " WHERE message.receiver_id = ?1" +
            " GROUP BY interlocutor_id, text)" +
            " UNION" +
            " (SELECT message.receiver_id as interlocutor_id, max(message.sent_time) as lastmsgtime, message.message_text as text" +
            " FROM message" +
            " WHERE message.sender_id = ?1" +
            " GROUP BY interlocutor_id, text)) as r" +
            " GROUP BY r.interlocutor_id) as f LEFT JOIN message m on f.lastmsgtime = m.sent_time LEFT JOIN jhi_user u on u.id = interlocutorid" +
            " ORDER BY messagetime DESC",
        nativeQuery = true)
    List<LatestUserMessage> getLastInterlocutorsWithMsg(Long currentUserId);


}
