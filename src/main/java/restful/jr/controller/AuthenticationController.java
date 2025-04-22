package restful.jr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restful.jr.dto.SignInRequestDTO;
import restful.jr.dto.TokenResponseDTO;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {

    @Operation(summary = "Access token", description = "Get access token by username and password")
    @PostMapping("/access-token")
    public TokenResponseDTO getAccessToken(@RequestBody SignInRequestDTO signInRequestDTO) {
        return TokenResponseDTO.builder()
                .accessToken("Fake-access-token").refreshToken("fake-fresh-token").build();
    }

    @Operation(summary = "Access token", description = "Get access token and fresh token")
    @PostMapping("/refresh-token")
    public TokenResponseDTO getRefreshToken(@RequestBody String refreshToken) {
        return TokenResponseDTO.builder()
                .accessToken("Dummy-new-access-token").refreshToken("Dummy-new-fresh-token").build();
    }
}
