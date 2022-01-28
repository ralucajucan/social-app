package org.utcn.socialapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.utcn.socialapp.auth.dto.AuthDTO;
import org.utcn.socialapp.auth.dto.CredentialsDTO;
import org.utcn.socialapp.auth.jwt.JwtUtility;
import org.utcn.socialapp.auth.refreshtoken.RefreshTokenService;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.email.EmailService;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserService;

import java.util.Objects;
import java.util.UUID;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_REQUEST;
import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthDTO authenticate(CredentialsDTO credentialsDTO) throws BusinessException {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(credentialsDTO.getEmail().trim(),
                            credentialsDTO.getPassword());
            User user = (User) authenticationManager.authenticate(token).getPrincipal();
            final String jwtToken = jwtUtility.generateToken(user);
            String refreshToken = refreshTokenService.createRefreshToken(user);
            return new AuthDTO(user, jwtToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new BusinessException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public void logout(Long userId) throws BusinessException {
        refreshTokenService.deleteByUserId(userId);
    }

    public boolean resetPassword(String email) throws BusinessException {
        if (!StringUtils.hasLength(email)) {
            throw new BusinessException(BAD_REQUEST);
        }
        User user = userService.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new BusinessException(NOT_FOUND);
        }
        final String newPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        try {
            userService.save(user);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }
        emailService.sendEmail(user.getEmail(), emailService.buildEmail(
                "Salut, " + user.getProfile().getFirstName(),
                "Mai jos este noua parola:",
                newPassword,
                ""));
        return true;
    }
}
