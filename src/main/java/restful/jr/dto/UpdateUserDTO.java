package restful.jr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserDTO {
    private String name;
    private String email;
}
