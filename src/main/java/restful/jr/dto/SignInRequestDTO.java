package restful.jr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class SignInRequestDTO  implements Serializable {
    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    private String platform;
    private String deviceToken;
    private String versionApp;
}
