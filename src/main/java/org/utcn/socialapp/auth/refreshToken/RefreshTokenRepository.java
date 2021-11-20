package org.utcn.socialapp.auth.refreshToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
    RefreshToken findByToken(String token);
}
