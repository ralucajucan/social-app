package org.utcn.socialapp.auth.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.email.EmailService;
import org.utcn.socialapp.email.EmailValidator;
import org.utcn.socialapp.profile.Profile;
import org.utcn.socialapp.profile.ProfileRepository;
import org.utcn.socialapp.user.Role;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.*;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private static final long REGISTER_TOKEN_VALIDITY_MINS = 15;
    private final EmailValidator emailValidator;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RegisterTokenService registerTokenService;

    /**
     * REGISTRATION:
     * --> USING SECURITY: Validate parameters, persist user and profile,
     * create token, send activation email, enable user.
     *
     * @param registerDTO Data transfer object
     * @return Token
     * @throws BusinessException with ClientErrorResponse as param
     */
    public RegisterToken register(RegisterDTO registerDTO) throws BusinessException {
        // Validate input:
        if (Objects.isNull(registerDTO) || registerDTO.anyMatchNull()) throw new BusinessException(BAD_REQUEST);
        if (Objects.nonNull(userRepository.findByEmail(registerDTO.getEmail()))) {
            // TODO if email not confirmed send confirmation email
            throw new BusinessException(CONFLICT_REGISTER);
        }

        if (!emailValidator.test(registerDTO.getEmail())) throw new BusinessException(BAD_CREDENTIALS);

        // Persist to database user and profile.
        User user = new User(registerDTO.getEmail(), bCryptPasswordEncoder.encode(registerDTO.getPassword()),
                Role.USER);
        Profile profile = new Profile(user, registerDTO.getFirstName(), registerDTO.getLastName(),
                LocalDate.parse(registerDTO.getBirthDate()));
        profileRepository.save(profile);

        // Send confirmation token:
        String uuid = UUID.randomUUID().toString();
        RegisterToken registerToken = new RegisterToken(user, uuid, Instant.now()
                .plus(REGISTER_TOKEN_VALIDITY_MINS, ChronoUnit.MINUTES));
        registerTokenService.saveToken(registerToken);

        // Send email:
        String link = "http://localhost:8080/api/auth/register/confirm?token=" + registerToken.getToken();
        emailService.sendRegisterToken(registerDTO.getEmail(),
                emailService.buildRegisterEmail(registerDTO.getFirstName(), link));

        return registerToken;
    }

    @Transactional
    public String enableUserWithToken(String uuid) throws BusinessException {
        RegisterToken registerToken = registerTokenService.getToken(uuid)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        if (Objects.nonNull(registerToken.getConfirmation())) {
            throw new BusinessException(CONFLICT_TOKEN);
        }
        Instant expiration = registerToken.getExpiration();
        if (expiration.isBefore(Instant.now())) {
            throw new BusinessException(EXPIRED_TOKEN);
        }
        registerToken.setConfirmation(Instant.now());
        registerTokenService.saveToken(registerToken);
        userRepository.enableUser(registerToken.getUser().getId());
        return "Confirmed!";
    }
}
