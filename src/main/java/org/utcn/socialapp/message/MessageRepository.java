package org.utcn.socialapp.message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.utcn.socialapp.user.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, MessagePK> {
    Long countAllBySenderAndReceiver(User sender, User receiver);

    @Query("select m from Message m where ((m.sender=?1 and m.receiver=?2) or (m.sender=?2 and m.receiver=?1))")
    List<Message> findConversation(User sender, User receiver, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Message m set m.status=org.utcn.socialapp.message.MessageStatus.RECEIVED" +
            " where (m.receiver=?1 and m.status=org.utcn.socialapp.message.MessageStatus.SENT)")
    void updateSentAsReceived(User receiver);

    @Transactional
    @Modifying
    @Query("update Message m set m.status=org.utcn.socialapp.message.MessageStatus.READ " +
            "where (m.sender=?1 and m.receiver=?2 and m.status=org.utcn.socialapp.message.MessageStatus.RECEIVED)")
    void updateReceivedAsRead(User sender, User receiver);

    Message findByStatus(MessageStatus status);
}

