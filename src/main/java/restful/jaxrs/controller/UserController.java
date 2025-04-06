package restful.jaxrs.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restful.jaxrs.dto.UpdateUserDTO;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.service.UserService;
import restful.jaxrs.util.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    //get all user
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    //add user
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> addUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO user = userService.addUser(userDTO);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    //update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO user = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    //patch user
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> patchUser(@PathVariable String id, @Valid @RequestBody UpdateUserDTO userDTO) {
        UserDTO user = userService.patchUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success(user));
    }


    //delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@PathVariable String id) {
        UserDTO userDTO = userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(userDTO));
    }
}
