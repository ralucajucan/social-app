package org.utcn.socialapp.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.email.EmailService;
import org.utcn.socialapp.email.EmailValidator;
import org.utcn.socialapp.profile.Profile;
import org.utcn.socialapp.profile.ProfileRepository;
import org.utcn.socialapp.user.registration.RegisterDTO;
import org.utcn.socialapp.user.registration.token.Token;
import org.utcn.socialapp.user.registration.token.TokenService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.*;

@Service
public class UserService implements UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final TokenService tokenService;
    private final EmailValidator emailValidator;
    private final EmailService emailService;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                       ProfileRepository profileRepository, TokenService tokenService, EmailValidator emailValidator,
                       EmailService emailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.tokenService = tokenService;
        this.emailValidator = emailValidator;
        this.emailService = emailService;
    }

    /**
     * Login by either email or username
     *
     * @param input = username/mail
     * @return UserDetails -> used in UserService, required by Spring Security.
     * @throws UsernameNotFoundException error thrown if user not found
     */
    @Override
    public User loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(input, input);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        if (!user.isAccountNonExpired()) throw new UsernameNotFoundException("User account expired!");
        if (!user.isEnabled()) throw new UsernameNotFoundException("User is disabled!");
        return user;
    }

    /**
     * REGISTRATION:
     * --> USING SECURITY: Validate parameters, persist user and profile,
     * create token, send activation email, enable user.
     *
     * @param registerDTO Data transfer object
     * @return Token
     * @throws BusinessException with ClientErrorResponse as param
     */
    public Token register(RegisterDTO registerDTO) throws BusinessException {
        // Validate input:
        if (Objects.isNull(registerDTO) || registerDTO.anyMatchNull())
            throw new BusinessException(BAD_REQUEST);
        if (Objects.nonNull(userRepository.findByUsernameOrEmail(registerDTO.getUsername(), registerDTO.getEmail())))
            throw new BusinessException(CONFLICT_REGISTER);
        if (!emailValidator.test(registerDTO.getEmail()))
            throw new BusinessException(BAD_CREDENTIALS);
        if (emailValidator.test(registerDTO.getUsername()))
            throw new BusinessException(BAD_CREDENTIALS);

        // Persist to database user and profile.
        User user = new User(registerDTO.getEmail(),
                registerDTO.getUsername(),
                bCryptPasswordEncoder.encode(registerDTO.getPassword()),
                Role.USER);
        Profile profile = new Profile(user, registerDTO.getFirstName(),
                registerDTO.getLastName(),
                LocalDate.parse(registerDTO.getBirthDate()));
        profileRepository.save(profile);

        // Send confirmation token:
        String uuid = UUID.randomUUID().toString();
        Token token = new Token(uuid,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
        tokenService.saveToken(token);

        // Send email:
        String link = "http://localhost:8080/register/confirm?token=" + token.getUuid();
        emailService.send(registerDTO.getEmail(), emailService.buildEmail(registerDTO.getFirstName(), link));

        return token;
    }

    @Transactional
    public String enableUserWithToken(String uuid) throws BusinessException {
        Token token = tokenService.getToken(uuid).orElseThrow(() -> new BusinessException(NOT_FOUND));
        if (Objects.nonNull(token.getConfirmation())) {
            throw new BusinessException(CONFLICT_TOKEN);
        }
        LocalDateTime expiration = token.getExpiration();
        if (expiration.isBefore(LocalDateTime.now())) {
            throw new BusinessException(EXPIRED_TOKEN);
        }
        token.setConfirmation(LocalDateTime.now());
        tokenService.saveToken(token);
        userRepository.enableUser(token.getUser().getId());
        return "Confirmed!";
    }
}