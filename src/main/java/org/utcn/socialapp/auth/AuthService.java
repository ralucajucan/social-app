package org.utcn.socialapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.utcn.socialapp.auth.dto.AuthDTO;
import org.utcn.socialapp.auth.dto.CredentialsDTO;
import org.utcn.socialapp.auth.dto.JwtDTO;
import org.utcn.socialapp.auth.dto.RegisterDTO;
import org.utcn.socialapp.auth.jwt.JwtUtility;
import org.utcn.socialapp.auth.refreshToken.RefreshToken;
import org.utcn.socialapp.auth.refreshToken.RefreshTokenService;
import org.utcn.socialapp.auth.registration.RegisterService;
import org.utcn.socialapp.auth.registration.RegisterToken;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserService;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_REQUEST;
import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RegisterService registerService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;

    public AuthDTO authenticate(CredentialsDTO credentialsDTO) throws BusinessException {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(credentialsDTO.getEmail().trim(),
                            credentialsDTO.getPassword());
            authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            throw new BusinessException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        final User user = userService.loadUserByUsername(credentialsDTO.getEmail());
        final String jwtToken = jwtUtility.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthDTO(user, jwtToken, refreshToken);
    }

    public RegisterToken register(RegisterDTO registerDTO) throws BusinessException {
        return registerService.register(registerDTO);
    }

    public String enableUserWithToken(String token) throws BusinessException {
        return registerService.enableUserWithToken(token);
    }

    public JwtDTO refreshJwtToken(String token) throws BusinessException {
        RefreshToken refreshToken = refreshTokenService.findByToken(token)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        refreshTokenService.verifyExpiration(refreshToken);
        final User user = refreshToken.getUser();
        final String jwtToken = jwtUtility.generateToken(user);

        return new JwtDTO(user, jwtToken);
    }

    public void logout(Long userId) throws BusinessException {
        refreshTokenService.deleteByUserId(userId);
    }

    public boolean resendToken(String email) throws BusinessException {
        if (!StringUtils.hasLength(email.trim())) {
            throw new BusinessException(BAD_REQUEST);
        }
        registerService.resendRegistrationToken(email.trim());
        return true;
    }

    public boolean resendPassword(String email) throws BusinessException{
        if (!StringUtils.hasLength(email.trim())) {
            throw new BusinessException(BAD_REQUEST);
        }
        return registerService.resendPassword(email.trim());
    }
}
