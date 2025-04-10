package restful.jaxrs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restful.jaxrs.dto.TaskDTO;
import restful.jaxrs.dto.UpdateTaskDTO;
import restful.jaxrs.entity.Project;
import restful.jaxrs.entity.Staff;
import restful.jaxrs.entity.Task;
import restful.jaxrs.exception.ResourceNotFoundException;
import restful.jaxrs.mapper.TaskMapper;
import restful.jaxrs.repository.ProjectRepository;
import restful.jaxrs.repository.StaffRepository;
import restful.jaxrs.repository.TaskRepository;
import restful.jaxrs.repository.UserRepository;
import restful.jaxrs.util.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskMapper taskMapper;

    Map<String, String> errors;
    @Autowired
    private StaffRepository staffRepository;

    //list all task
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::toDTO).collect(Collectors.toList());
    }

    //get task by id
    public TaskDTO getTaskById(String id) {
        Long taskId = utility.validateAndConvertId(id);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toDTO(task);
    }

    //add task
    public TaskDTO addTask(TaskDTO taskDTO) {
        errors = new HashMap<String, String>();

        Task task = taskMapper.toEntity(taskDTO);
        if (taskRepository.existsByTitle(taskDTO.getTitle())) {
            errors.put("title", "Task with name '" + taskDTO.getTitle() + "' already exists");
        }

        if (taskDTO.getStaffId() != null) {
            staffRepository.findById(taskDTO.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + taskDTO.getStaffId()));
        }
        if (taskDTO.getProjectId() != null) {
            projectRepository.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDTO.getProjectId()));
        }

        //check user
        task = taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    //update task
    public TaskDTO updateTask(String id, TaskDTO taskDTO) {
        Long taskId = utility.validateAndConvertId(id);
        //check task by id
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setDescription(taskDTO.getDescription());

        existingTask.setStaffId(staffRepository.findById(taskDTO.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDTO.getStaffId())).getId()
        );

        existingTask.setProjectId(projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDTO.getProjectId())).getId()
        );

        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toDTO(updatedTask);
    }

    //delete task
    public void deleteTask(String id) {
        Long taskId = utility.validateAndConvertId(id);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
    }


    //patch task
    public TaskDTO patchTask(String id, UpdateTaskDTO updateTaskDTO) {
        Long taskId = utility.validateAndConvertId(id);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        //only update field have in DTO
        if (updateTaskDTO.getTitle() != null) {
            existingTask.setTitle(updateTaskDTO.getTitle());
        }

        if (updateTaskDTO.getDescription() != null) {
            existingTask.setDescription(updateTaskDTO.getDescription());
        }
        if (updateTaskDTO.getStatus() != null) {
            existingTask.setStatus(updateTaskDTO.getStatus());
        }

        if (updateTaskDTO.getStaffId() != null) {
            Staff staff = staffRepository.findById(updateTaskDTO.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + updateTaskDTO.getStaffId()));
            existingTask.setStaffId(staff.getId());
        }

        if (updateTaskDTO.getProjectId() != null) {
            Project project = projectRepository.findById(updateTaskDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + updateTaskDTO.getProjectId()));
            existingTask.setProjectId(project.getId());
        }

        Task updatedTask = taskRepository.save(existingTask);

        return taskMapper.toDTO(updatedTask);
    }

}
