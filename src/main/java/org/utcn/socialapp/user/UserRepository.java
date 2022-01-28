package org.utcn.socialapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.user.profile.Profile;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying
    @Query("UPDATE User user SET user.enabled = TRUE WHERE user.id = ?1")
    void enableUser(Long id);

    @Query("SELECT p FROM Profile p WHERE p.id= :id")
    Optional<Profile> findProfileByUserId(Long id);

    @Query("select u from User u where u.enabled=true and u.locked=false")
    List<User> findAllActive();
}
