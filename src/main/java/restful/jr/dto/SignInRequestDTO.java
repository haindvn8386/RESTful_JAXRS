package restful.jr.dto;

import java.io.Serializable;

public class SignInRequestDTO  implements Serializable {
    private String username;
    private String password;
    private String platform;
    private String deviceToken;
    private String versionApp;
}
