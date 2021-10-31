package org.utcn.socialapp.service.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.utcn.socialapp.config.exception.BusinessException;
import org.utcn.socialapp.config.exception.ClientErrorResponse;
import org.utcn.socialapp.model.Profile;
import org.utcn.socialapp.model.User;
import org.utcn.socialapp.model.dto.RegisterDTO;
import org.utcn.socialapp.model.security.Role;
import org.utcn.socialapp.model.security.Token;
import org.utcn.socialapp.repository.TokenRepository;
import org.utcn.socialapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    public CustomUserDetailsService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Login by either email or username
     *
     * @param input = username/mail
     * @return UserDetails -> used in UserService, required by Spring Security.
     * @throws UsernameNotFoundException
     */
    @Override
    public User loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(input);
        if (user == null) user = userRepository.findByUsername(input);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user;
    }

    /**
     * REGISTRATION:
     * --> USING SECURITY: Validate parameters, persist user and profile,
     * create token, send activation email, enable user.
     *
     * @param registerDTO
     * @return Token
     * @throws BusinessException with ClientErrorResponse as param
     */
    public Token registerUser(RegisterDTO registerDTO) throws BusinessException {
        if (Objects.isNull(registerDTO) || registerDTO.anyMatchNull())
            throw new BusinessException(ClientErrorResponse.BAD_REQUEST);
        if (Objects.nonNull(userRepository.findByEmail(registerDTO.getEmail())) ||
                Objects.nonNull(userRepository.findByUsername(registerDTO.getUsername())))
            throw new BusinessException(ClientErrorResponse.CONFLICT_USER);
        if (Pattern.matches("/\\A[^@]+@([^@\\.]+\\.)+[^@\\.]+\\z/", registerDTO.getEmail()))
            throw new BusinessException(ClientErrorResponse.BAD_REQUEST);

        // Persist to database user and profile.
        User user = new User(registerDTO.getEmail(),
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                Role.USER);
        Profile profile = new Profile(registerDTO.getFirstName(),
                registerDTO.getLastName(),
                registerDTO.getBirthDate());
        user.setProfile(profile);
        userRepository.save(user);

        // Send confirmation token:
        UUID uuid = UUID.randomUUID();
        Token token = new Token(uuid,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
        tokenRepository.save(token);
        return token;
    }
}
