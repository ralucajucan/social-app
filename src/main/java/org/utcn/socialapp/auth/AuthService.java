package org.utcn.socialapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.utcn.socialapp.auth.jwt.JWTResponse;
import org.utcn.socialapp.auth.jwt.JWTUtility;
import org.utcn.socialapp.auth.refreshToken.RefreshToken;
import org.utcn.socialapp.auth.refreshToken.RefreshTokenService;
import org.utcn.socialapp.auth.registration.RegisterDTO;
import org.utcn.socialapp.auth.registration.RegisterService;
import org.utcn.socialapp.auth.registration.RegisterToken;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserService;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RegisterService registerService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtility jwtUtility;

    public AuthResDTO authenticate(AuthDTO authDTO) throws BusinessException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),
                    authDTO.getPassword()));
        } catch (AuthenticationException e) {
            throw new BusinessException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        final User user = userService.loadUserByUsername(authDTO.getEmail());
        final String jwtToken = jwtUtility.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResDTO(jwtToken, refreshToken, user.getId(), user.getEmail(), user.getRole()
                                                                                         .toString());
    }

    public RegisterToken register(RegisterDTO registerDTO) throws BusinessException {
        return registerService.register(registerDTO);
    }

    public String enableUserWithToken(String token) throws BusinessException {
        return registerService.enableUserWithToken(token);
    }

    public JWTResponse refreshJwtToken(String token) throws BusinessException {
        RefreshToken refreshToken = refreshTokenService.findByToken(token)
                                                       .orElseThrow(() -> new BusinessException(NOT_FOUND));
        refreshTokenService.verifyExpiration(refreshToken);
        final User user = refreshToken.getUser();
        final String jwtToken = jwtUtility.generateToken(user);

        return new JWTResponse(jwtToken);
    }

    public void logout(Long userId) throws BusinessException {
        refreshTokenService.deleteByUserId(userId);
    }
}
