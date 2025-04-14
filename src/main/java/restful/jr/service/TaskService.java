package restful.jr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restful.jr.dto.TaskDTO;
import restful.jr.dto.UpdateTaskDTO;
import restful.jr.entity.Project;
import restful.jr.entity.Task;
import restful.jr.exception.ResourceNotFoundException;
import restful.jr.mapper.TaskMapper;
import restful.jr.repository.ProjectRepository;
import restful.jr.repository.StaffRepository;
import restful.jr.repository.TaskRepository;
import restful.jr.util.utility;

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

        if (taskDTO.getProject() != null) {
            projectRepository.findById(taskDTO.getProject().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDTO.getProject().getId()));
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
        existingTask.setStatusStaff(taskDTO.getStatusStaff());
        existingTask.setDescription(taskDTO.getDescription());

        existingTask.setProject(projectRepository.findById(taskDTO.getProject().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDTO.getProject().getId()))
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
        if (updateTaskDTO.getStatusStaff() != null) {
            existingTask.setStatusStaff(updateTaskDTO.getStatusStaff());
        }

        if (updateTaskDTO.getProject() != null) {
            Project project = projectRepository.findById(updateTaskDTO.getProject().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + updateTaskDTO.getProject().getId()));
            existingTask.setProject(project);
        }

        Task updatedTask = taskRepository.save(existingTask);

        return taskMapper.toDTO(updatedTask);
    }

}
