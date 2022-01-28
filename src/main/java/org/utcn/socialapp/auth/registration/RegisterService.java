package org.utcn.socialapp.auth.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.email.EmailService;
import org.utcn.socialapp.email.EmailValidator;
import org.utcn.socialapp.user.Role;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserRepository;
import org.utcn.socialapp.user.profile.Profile;
import org.utcn.socialapp.user.profile.ProfileRepository;

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
    private final RegisterRepository registerRepository;

    /**
     * REGISTRATION:
     * --> USING SECURITY: Validate parameters, persist user and profile,
     * create token, send activation email, enable user.
     *
     * @param registerDTO Data transfer object
     * @return Token
     * @throws BusinessException with ClientErrorResponse as param
     */
    public Register register(RegisterDTO registerDTO) throws BusinessException {
        // Validate input:
        if (Objects.isNull(registerDTO) || registerDTO.anyMatchNull()) throw new BusinessException(BAD_REQUEST);
        if (Objects.nonNull(userRepository.findByEmail(registerDTO.getEmail().trim()))) {
            throw new BusinessException(CONFLICT_REGISTER);
        }

        if (!emailValidator.test(registerDTO.getEmail().trim())) throw new BusinessException(BAD_CREDENTIALS);

        // Persist to database user and profile.
        User user = new User(registerDTO.getEmail().trim(), bCryptPasswordEncoder.encode(registerDTO.getPassword()),
                Role.USER);
        Profile profile = new Profile(user, registerDTO.getFirstName().trim(), registerDTO.getLastName().trim(),
                LocalDate.parse(registerDTO.getBirthDate()));
        profileRepository.save(profile);

        // Send confirmation token:
        Register register = new Register(
                user,
                UUID.randomUUID().toString(),
                Instant.now().plus(REGISTER_TOKEN_VALIDITY_MINS, ChronoUnit.MINUTES)
        );
        registerRepository.save(register);

        // Send email:
        String link = "http://localhost:4200/register?token=" + register.getToken();
        emailService.sendEmail(registerDTO.getEmail().trim(),
                emailService.buildEmail(
                        "Sunteti la un pas distanta de inregistrare, " + registerDTO.getFirstName(),
                        "Confirmati adresa dand click mai jos:",
                        "Click pe link-ul de activare <-",
                        link));

        return register;
    }

    @Transactional
    public String enableUserWithToken(String uuid) throws BusinessException {
        Register register = registerRepository.findByToken(uuid)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        if (Objects.nonNull(register.getConfirmation())) {
            throw new BusinessException(CONFLICT_TOKEN);
        }
        Instant expiration = register.getExpiration();
        if (expiration.isBefore(Instant.now())) {
            throw new BusinessException(EXPIRED_TOKEN);
        }
        register.setConfirmation(Instant.now());
        registerRepository.save(register);
        userRepository.enableUser(register.getUser().getId());
        return "Confirmed!";
    }

    public boolean resetRegisterToken(String email) throws BusinessException {
        if (!StringUtils.hasLength(email)) {
            throw new BusinessException(BAD_REQUEST);
        }
        User user = userRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new BusinessException(NOT_FOUND);
        }
        if (user.isEnabled()) {
            throw new BusinessException(NOT_ALLOWED); //already enabled
        }
        Register register = registerRepository
                .findByUser(user).orElseThrow(() -> new BusinessException(NOT_FOUND));
        String token = UUID.randomUUID().toString();
        while (registerRepository.findByToken(token).isPresent()) {
            token = UUID.randomUUID().toString();
        }
        register.setToken(token);
        register.setExpiration(Instant.now().plus(REGISTER_TOKEN_VALIDITY_MINS, ChronoUnit.MINUTES));
        try {
            registerRepository.save(register);
        }catch(Exception e){
            throw new BusinessException(e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }
        //http://localhost:8080/api/auth/register/confirm?token=
        String link = "http://localhost:4200/register?token=" + register.getToken();
        emailService.sendEmail(user.getEmail(), emailService.buildEmail(
                "Sunteti la un pas distanta de inregistrare, " + user.getProfile().getFirstName(),
                "Confirmati adresa dand click mai jos:",
                "Click pe link-ul de activare <-",
                link));
        return true;
    }
}
