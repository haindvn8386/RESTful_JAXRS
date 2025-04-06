package restful.jaxrs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import restful.jaxrs.entity.Task;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Email is mandatory")
    private String email;

    private List<TaskDTO> tasks;
}
