package restful.jaxrs.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restful.jaxrs.dto.TaskDTO;
import restful.jaxrs.dto.UpdateTaskDTO;
import restful.jaxrs.service.TaskService;
import restful.jaxrs.util.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //get all Task
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getAllTasks() {
        return ResponseEntity.ok(ApiResponse.success(200, "Tasks retrieved successfully", taskService.getAllTasks()));
    }

    //get Task by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(200, "Task retrieved successfully", taskService.getTaskById(id)));
    }

    //add Task
    @PostMapping
    public ResponseEntity<ApiResponse<TaskDTO>> addUser(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO user = taskService.addTask(taskDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED.value(), "Task created successfully", user));
    }

    //update Task
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> updateUser(@PathVariable String id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO task = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(ApiResponse.success(200, "Task Updated successfully", task));
    }

    //patch Task
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> patchUser(@PathVariable String id, @Valid @RequestBody UpdateTaskDTO updateTaskDTO) {
        TaskDTO task = taskService.patchTask(id, updateTaskDTO);
        return ResponseEntity.ok(ApiResponse.success(200, "Task Updated successfully", task));
    }

    //delete Task
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.ACCEPTED.value(), "Task deleted successfully", id));
    }
}
