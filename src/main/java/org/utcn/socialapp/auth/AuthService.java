package org.utcn.socialapp.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.utcn.socialapp.auth.registration.RegisterDTO;
import org.utcn.socialapp.auth.registration.RegisterService;
import org.utcn.socialapp.auth.registration.RegisterToken;
import org.utcn.socialapp.common.exception.BusinessException;
import org.utcn.socialapp.common.utils.JWTUtility;
import org.utcn.socialapp.user.User;
import org.utcn.socialapp.user.UserService;

import static org.utcn.socialapp.common.exception.ClientErrorResponse.BAD_CREDENTIALS;

@Service
public class AuthService {
    private final UserService userService;
    private final RegisterService registerService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtility jwtUtility;

    public AuthService(UserService userService, RegisterService registerService,
                       AuthenticationManager authenticationManager, JWTUtility jwtUtility) {
        this.userService = userService;
        this.registerService = registerService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
    }

    public String authenticate (AuthDTO authDTO) throws BusinessException{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDTO.getEmail(),
                            authDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BusinessException(BAD_CREDENTIALS);
        }

        final User user = userService.loadUserByUsername(authDTO.getEmail());

        final String jwtToken = jwtUtility.generateToken(user);

        return jwtToken;
    }

    public RegisterToken register(RegisterDTO registerDTO) throws BusinessException {
        return registerService.register(registerDTO);
    }

    public String enableUserWithToken(String token) throws BusinessException{
        return registerService.enableUserWithToken(token);
    }
}
