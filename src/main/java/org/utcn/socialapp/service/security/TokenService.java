package org.utcn.socialapp.service.security;

import org.springframework.stereotype.Service;
import org.utcn.socialapp.model.security.Token;
import org.utcn.socialapp.repository.TokenRepository;

import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Optional<Token> getToken(String uuid) {
        return tokenRepository.findByUuid(uuid);
    }
}
