package org.utcn.socialapp.message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.user.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, MessagePK> {
    Long countAllBySenderAndReceiver(User sender, User receiver);

    @Query("select m from Message m where ((m.sender=?1 and m.receiver=?2) or (m.sender=?2 and m.receiver=?1))")
    List<Message> findConversation(User sender, User receiver, Pageable pageable);
}

