package org.utcn.socialapp.user.profile;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.user.User;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}
