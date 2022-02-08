package org.utcn.socialapp.message.attachment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, String> {
    @Query("{'id':{$in: ?0 }}")
    List<Attachment> findByMultipleIds(String[] ids);
}
