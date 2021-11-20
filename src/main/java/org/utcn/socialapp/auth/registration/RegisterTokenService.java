package org.utcn.socialapp.auth.registration;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterTokenService {
    private final RegisterTokenRepository registerTokenRepository;

    public RegisterTokenService(RegisterTokenRepository registerTokenRepository) {
        this.registerTokenRepository = registerTokenRepository;
    }

    public void saveToken(RegisterToken registerToken) {
        registerTokenRepository.save(registerToken);
    }

    public Optional<RegisterToken> getToken(String token) {
        return registerTokenRepository.findByToken(token);
    }
}
