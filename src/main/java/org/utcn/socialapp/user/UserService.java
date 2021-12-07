package org.utcn.socialapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Login by either email or username
     *
     * @param input = username/mail
     * @return UserDetails -> used in UserService, required by Spring Security.
     * @throws UsernameNotFoundException error thrown if user not found
     */
    @Override
    public User loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(input);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        if (!user.isAccountNonExpired()) throw new UsernameNotFoundException("User account expired!");
        if (!user.isEnabled()) throw new UsernameNotFoundException("User is disabled!");
        return user;
    }
}