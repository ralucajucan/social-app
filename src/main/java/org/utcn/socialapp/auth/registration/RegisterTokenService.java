package org.utcn.socialapp.auth.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterTokenService {
    private final RegisterTokenRepository registerTokenRepository;

    public void saveToken(RegisterToken registerToken) {
        registerTokenRepository.save(registerToken);
    }

    public Optional<RegisterToken> getToken(String token) {
        return registerTokenRepository.findByToken(token);
    }
}
