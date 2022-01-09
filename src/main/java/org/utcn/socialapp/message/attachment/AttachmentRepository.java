package org.utcn.socialapp.message.attachment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, String> {
    Optional<Attachment> findByName(String name);

    @Query("{'id':{$in: ?0 }}")
    List<Attachment> findByMultipleIds(String[] ids);

    // TODO: find use, Maybe a bot that chats with u, has a list of locations and sends u the text associated with location?
//    List<Person> findByLocationNear(Point location, Distance distance);
}
