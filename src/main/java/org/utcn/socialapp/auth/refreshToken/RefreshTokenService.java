package org.utcn.socialapp.auth.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utcn.socialapp.auth.dto.JwtDTO;
import org.utcn.socialapp.auth.jwt.JwtUtility;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.User;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.EXPIRED_TOKEN;
import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements Serializable {
    private static final long serialVersionUID = 234234523654L;
    private static final long REFRESH_TOKEN_VALIDITY_DAY = 90;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtility jwtUtility;

    @Transactional
    public String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        while (refreshTokenRepository.findByToken(token).isPresent()) {
            token = UUID.randomUUID().toString();
        }
        Instant expiration = Instant.now()
                .plus(REFRESH_TOKEN_VALIDITY_DAY, ChronoUnit.DAYS);
        RefreshToken refreshToken = user.getRefreshToken();
        if (Objects.nonNull(refreshToken)) {
            refreshToken.setToken(token);
            refreshToken.setExpiration(expiration);
            return refreshTokenRepository.save(refreshToken)
                    .getToken();
        }
        return refreshTokenRepository.save(new RefreshToken(user, token, expiration))
                .getToken();
    }

    private void verifyExpiration(RefreshToken token) throws BusinessException {
        if (token.getExpiration()
                .isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BusinessException(EXPIRED_TOKEN);
        }
    }

    public JwtDTO refreshJwtToken(String token) throws BusinessException {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        this.verifyExpiration(refreshToken);
        final User user = refreshToken.getUser();
        final String jwtToken = jwtUtility.generateToken(user);

        return new JwtDTO(user, jwtToken);
    }

    @Transactional
    public void deleteByUserId(Long userId) throws BusinessException {
        refreshTokenRepository.delete(refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND)));
    }
}
