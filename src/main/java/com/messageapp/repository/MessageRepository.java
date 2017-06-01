package com.messageapp.repository;

import com.messageapp.domain.Message;
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


    @Query(value = "select m from Message m where" +
            " m.receiver.login = ?#{principal.username} and m.sender.login = :interlocutorLogin" +
            " or m.receiver.login = :interlocutorLogin and m.sender.login = ?#{principal.username}" +
            " order by m.sentTime desc")
    List<Message> findMessagesForConversation(@Param("interlocutorLogin") String interlocutorLogin);



    @Query(value =
        "SELECT u.login as login, m.message_text as message, f.lstmsgtime as messagetime FROM" +
            " (SELECT r.interlocutor_id as interlocutorid, max(r.lastmsgtime) as lstmsgtime" +
            " FROM" +
            " ((SELECT message.sender_id as interlocutor_id, max(message.sent_time) as lastmsgtime, message.message_text as text" +
            " FROM MESSAGE" +
            " WHERE message.receiver_id = ?1" +
            " GROUP BY interlocutor_id, text)" +
            " UNION" +
            " (SELECT message.receiver_id as interlocutor_id, max(message.sent_time) as lastmsgtime, message.message_text as text" +
            " FROM MESSAGE" +
            " WHERE message.sender_id = ?1" +
            " GROUP BY interlocutor_id, text)) as r" +
            " GROUP BY r.interlocutor_id) as f LEFT JOIN message m on f.lstmsgtime = m.sent_time left join jhi_user u on u.id = interlocutorid" +
            " ORDER BY messagetime DESC",
        nativeQuery = true)
    List<LatestUserMessage> getLastInterlocutorsWithMsg(Long currentUserId);


    //UNUSED
    //Using nativeQuery because lack of limitation in JPQL
    @Query(value = "SELECT * FROM Message " +
            "WHERE sender_id = ?1 AND receiver_id = ?2 " +
            "OR sender_id = ?2 AND receiver_id = ?1 " +
            "ORDER BY sent_time DESC " +
            "LIMIT 1", nativeQuery = true)
    Message findLatestMessageForConversation(Long userId, Long interlocutorId);

}
