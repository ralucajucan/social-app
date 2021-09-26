package org.utcn.socialapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.model.Request;
import org.utcn.socialapp.model.RequestPK;

@Repository
public interface RequestRepository extends CrudRepository<Request, RequestPK> {
}
