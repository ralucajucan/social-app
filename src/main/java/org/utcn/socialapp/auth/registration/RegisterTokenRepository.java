package org.utcn.socialapp.auth.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterTokenRepository extends JpaRepository<RegisterToken, Long> {
    Optional<RegisterToken> findByToken(String token);
}
