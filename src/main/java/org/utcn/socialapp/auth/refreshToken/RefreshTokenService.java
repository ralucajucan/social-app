package org.utcn.socialapp.auth.refreshToken;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utcn.socialapp.common.exception.BusinessException;

import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.*;

@Service
public class RefreshTokenService implements Serializable {
    private static final long serialVersionUID = 234234523654L;
    private static final long REFRESH_TOKEN_VALIDITY_DAY = 90;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }


    @Transactional
    public String createRefreshToken(User user){
        String token = UUID.randomUUID().toString();
        OffsetDateTime expiration = OffsetDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAY);
        RefreshToken refreshToken = user.getRefreshToken();
        if(Objects.nonNull(refreshToken)){
            refreshToken.setToken(token);
            refreshToken.setExpiration(expiration);
            return refreshTokenRepository.save(refreshToken).getToken();
        }
        return refreshTokenRepository.save(new RefreshToken(user,token,expiration)).getToken();
    }

    public void verifyExpiration(RefreshToken token) throws BusinessException{
        if (token.getExpiration().isBefore(OffsetDateTime.now())){
            refreshTokenRepository.delete(token);
            throw new BusinessException(EXPIRED_TOKEN);
        }
    }

    @Transactional
    public void deleteByUserId(Long userId) throws BusinessException{
        refreshTokenRepository.delete(refreshTokenRepository.findById(userId).orElseThrow(()->new BusinessException(NOT_FOUND)));
    }
}
