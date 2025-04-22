package restful.jr.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restful.jr.dto.SignInRequestDTO;
import restful.jr.dto.TokenResponseDTO;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationInterface {

    //private final UserRepository userRepository;
    //private final AuthenticationManager authenticationManager;
    //private final JwtService jwtService;

    @Override
    public TokenResponseDTO getAccessToken(SignInRequestDTO signInRequestDTO) {
        return null;
    }

    @Override
    public TokenResponseDTO getRefreshToken(String request) {
        return null;
    }
}
