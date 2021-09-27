package org.utcn.socialapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.utcn.socialapp.model.User;

@NoRepositoryBean
public interface UserRepository extends CrudRepository<User, Long> {
}
