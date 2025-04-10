package restful.jaxrs.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restful.jaxrs.dto.UpdateUserDTO;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.service.UserService;
import restful.jaxrs.util.ApiResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;


    //get all user
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(@RequestParam(name = "page", defaultValue = "1") int page
            , @RequestParam(required = false) String sortBy, @RequestParam(required = false) String search) {

        Page<UserDTO> userPage = userService.getAllUsers(page, sortBy, search);
        return ResponseEntity.ok(ApiResponse.success(200, "Users retrieved successfully", userPage));
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(200, "User retrieved successfully", userService.getUserById(id)));
    }

    //add user
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> addUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO user = userService.addUser(userDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED.value(), "User created successfully", user));
    }

    //update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO user = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success(200, "User updated successfully", user));
    }

    //patch user
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> patchUser(@PathVariable String id, @Valid @RequestBody UpdateUserDTO userDTO) {
        UserDTO user = userService.patchUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success(200, "User updated successfully", user));
    }


    //delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@PathVariable String id) {
        UserDTO userDTO = userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.ACCEPTED.value(), "User deleted successfully", userDTO));
    }

}
