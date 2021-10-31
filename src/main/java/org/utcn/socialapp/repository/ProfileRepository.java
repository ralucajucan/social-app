package org.utcn.socialapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.model.Profile;
import org.utcn.socialapp.model.security.User;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Profile findByUser(User user);
}
