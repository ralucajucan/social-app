package org.utcn.socialapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.user.dto.EditDTO;
import org.utcn.socialapp.user.dto.PasswordDTO;
import org.utcn.socialapp.user.dto.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_REQUEST;
import static org.utcn.socialapp.common.exception.ClientErrorResponse.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Login by either email or username
     *
     * @param input = username/mail
     * @return UserDetails -> used in UserService, required by Spring Security.
     * @throws UsernameNotFoundException error thrown if user not found
     */
    @Override
    public User loadUserByUsername(String input) throws UsernameNotFoundException, LockedException, DisabledException {
        User user = userRepository.findByEmail(input);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        if (!user.isAccountNonExpired()) throw new LockedException("User account expired!");
        if (!user.isEnabled()) throw new DisabledException("User email not confirmed!");
        return user;
    }

    public void changePassword(PasswordDTO passwordDTO) throws BusinessException {
        if (Objects.isNull(passwordDTO) || passwordDTO.requiredAnyMatchNull()) {
            throw new BusinessException(BAD_REQUEST);
        }
        User user = userRepository.findById(passwordDTO.getId()).orElseThrow(() -> new BusinessException(NOT_FOUND));
        final boolean matches = bCryptPasswordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword());
        if (!matches) {
            throw new BusinessException(BAD_REQUEST);
        }
        user.setPassword(bCryptPasswordEncoder.encode(passwordDTO.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    public void changeSelected(EditDTO editDTO) throws BusinessException {
        if (Objects.isNull(editDTO) || editDTO.requiredAnyMatchNull()) {
            throw new BusinessException(BAD_REQUEST);
        }
        User user = userRepository.findById(editDTO.getId()).orElseThrow(() -> new BusinessException(NOT_FOUND));
        switch (editDTO.getSelected()) {
            case "firstName": {
                user.getProfile().setFirstName(editDTO.getChange());
                break;
            }
            case "lastName": {
                user.getProfile().setLastName(editDTO.getChange());
                break;
            }
            case "birthDate": {
                user.getProfile().setBirthDate(LocalDate.parse(editDTO.getChange()));
                break;
            }
            case "biography": {
                user.getProfile().setBiography(editDTO.getChange());
                break;
            }
            default: {
                throw new BusinessException(BAD_REQUEST);
            }
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    public List<UserDTO> getUserPage(int page, int count) throws BusinessException {
        if (page < 0 || count < 1) {
            throw new BusinessException(BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, count, Sort.by("email").ascending());
        return userRepository.findAllByPage(pageable).stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public void deleteUser(Long id) {

    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User save(User user){
        return userRepository.save(user);
    }
}