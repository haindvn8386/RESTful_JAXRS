package restful.jr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restful.jr.dto.RefreshTokenRequestDTO;
import restful.jr.dto.SignInRequestDTO;
import restful.jr.dto.TokenResponseDTO;
import restful.jr.service.auth.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;



    @Operation(summary = "Access token", description = "Get access token by username and password")
    @PostMapping("/access-token")
    public TokenResponseDTO getAccessToken(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        return authenticationService.getAccessToken(signInRequestDTO);
    }

    @Operation(summary = "Access token", description = "Get access token and fresh token")
    @PostMapping("/refresh-token")
    public TokenResponseDTO getRefreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenDTO) {
        return authenticationService.getRefreshToken(refreshTokenDTO);
    }
}
