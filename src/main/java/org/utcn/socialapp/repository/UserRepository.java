package org.utcn.socialapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.model.Profile;
import org.utcn.socialapp.model.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail(String username, String email);

    User findByEmail(String email);

    @Modifying
    @Query("UPDATE User user SET user.enabled = TRUE WHERE user.id = ?1")
    int enableUser(Long id);

    @Query("SELECT p FROM Profile p WHERE p.id= :user")
    Profile findProfileByUser(User user);
}
