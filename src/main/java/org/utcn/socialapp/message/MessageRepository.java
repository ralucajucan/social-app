package org.utcn.socialapp.message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.utcn.socialapp.message.dto.MessageDTO;
import org.utcn.socialapp.user.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, MessagePK> {
    Long countAllBySenderAndReceiver(User sender, User receiver);

    @Query("select new org.utcn.socialapp.message.dto.MessageDTO(m.messagePK.id, u1.email, u2.email, m.text, m" +
            ".attachmentIds, m.status, m.edited,m.audit.createdOn,m.audit.updatedOn) from Message m inner join User " +
            "u1 on u1.id=m.messagePK.senderId inner join User u2 on u2.id=m.messagePK.receiverId where (m.sender=?1 " +
            "and m.receiver=?2) or (m.sender=?2 and m.receiver=?1) and m.status<>org.utcn.socialapp.message" +
            ".MessageStatus.DRAFT")
    List<MessageDTO> findConversation(User principal, User contact, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Message m set m.status=org.utcn.socialapp.message.MessageStatus.RECEIVED" +
            " where (m.receiver=?1 and m.status=org.utcn.socialapp.message.MessageStatus.SENT)")
    void updateSentAsReceived(User receiver);

    @Transactional
    @Modifying
    @Query("update Message m set m.status=org.utcn.socialapp.message.MessageStatus.READ " +
            "where (m.receiver=?1 and m.sender=?2 and m.status=org.utcn.socialapp.message.MessageStatus.RECEIVED)")
    void updateReceivedAsRead(User principal, User contact);

    @Query("select m from Message m where m.sender.email=?1 and m.receiver.email=?2 and m.status=org.utcn.socialapp" +
            ".message.MessageStatus.DRAFT")
    Message findDraft(String principalEmail, String contactEmail);

    @Query("select count(m) from Message m where m.sender=?1 and m.status<>?2")
    Long countSenderExcludeStatus(User user, MessageStatus messageStatus);

    Message findByAttachmentIdsContains(String id);

    @Query("select m from Message m inner join User u1 on u1.id=m.messagePK.senderId inner join User u2 on u2.id=m" +
            ".messagePK.receiverId where m.messagePK.id=?1 and u1.email=?2 and u2.email=?3")
    Message findByIdAndEmails(Long id, String principalEmail, String contactEmail);
}

