package org.utcn.socialapp.auth.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.utcn.socialapp.user.User;

import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {
    Optional<Register> findByToken(String token);
    Optional<Register> findByUser(User user);
}
