package restful.jr.service.auth;

import restful.jr.dto.RefreshTokenRequestDTO;
import restful.jr.dto.SignInRequestDTO;
import restful.jr.dto.TokenResponseDTO;

public interface AuthenticationInterface {
    TokenResponseDTO getAccessToken(SignInRequestDTO signInRequestDTO);

    TokenResponseDTO getRefreshToken(RefreshTokenRequestDTO refreshTokenDTO);
}
