package org.utcn.socialapp.profile;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.user.User;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Profile findByUser(User user);
}
