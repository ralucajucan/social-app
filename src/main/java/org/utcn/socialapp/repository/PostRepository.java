package org.utcn.socialapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.model.Post;
import org.utcn.socialapp.model.PostPK;

@Repository
public interface PostRepository extends CrudRepository<Post, PostPK> {
}
