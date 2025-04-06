package restful.jaxrs.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restful.jaxrs.dto.ProjectDTO;
import restful.jaxrs.dto.UpdateProjectDTO;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.entity.Project;
import restful.jaxrs.entity.User;
import restful.jaxrs.exception.ResourceNotFoundException;
import restful.jaxrs.mapper.ProjectMapper;
import restful.jaxrs.mapper.UserMapper;
import restful.jaxrs.repository.ProjectRepository;
import restful.jaxrs.repository.UserRepository;
import restful.jaxrs.util.utility;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;


    //all project
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

    //get project by id
    public ProjectDTO getProjectById(String id) {
        Long projectId = utility.validateAndConvertId(id);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        return projectMapper.toDTO(project);
    }

    //create project
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = projectMapper.toEntity(projectDTO);
        Project saved = projectRepository.save(project);
        return projectMapper.toDTO(saved);
    }

    //update project
    public ProjectDTO updateProject(String id, ProjectDTO projectDTO) {

        Long projectId = utility.validateAndConvertId(id);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStatus(projectDTO.getStatus());

        //update manager
        if (projectDTO.getManager() != null && projectDTO.getManager().getId() != null) {
            User manager = userRepository.findById(projectDTO.getManager().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + projectDTO.getManager().getId()));
            project.setManager(manager);
        } else {
            throw new IllegalArgumentException("Project not found with id: " + id);
        }

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDTO(updatedProject);
    }

    //delete project
    public void deleteProject(String id) {
        Long projectId = utility.validateAndConvertId(id);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
    }


    //patch project
    public ProjectDTO patchProject(String id, UpdateProjectDTO updateProjectDTO) {
        Long projectId = utility.validateAndConvertId(id);
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        //only update field have in DTO
        if (updateProjectDTO.getName() != null) {
            existingProject.setName(updateProjectDTO.getName());
        }
        if (updateProjectDTO.getDescription() != null) {
            existingProject.setDescription(updateProjectDTO.getDescription());
        }
        if (updateProjectDTO.getStatus() != null) {
            existingProject.setStatus(updateProjectDTO.getStatus());
        }
        if (updateProjectDTO.getManager() != null) {
            User manager = userRepository.findById(updateProjectDTO.getManager().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + updateProjectDTO.getManager().getId()));
            existingProject.setManager(manager);
        }
        Project updatedProject = projectRepository.save(existingProject);
        return projectMapper.toDTO(updatedProject);
    }

    /// //////////////////////
    /// //////////////////////MEMBER SERVICE
    /// //////////////////////
    //add member to project
    public List<Long> addMembersInProject(String id, List<Long> membersId) {
        Long projectId = utility.validateAndConvertId(id);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        List<User> users = membersId.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId)))
                .collect(Collectors.toList());

        project.setMembers(users);
        projectRepository.save(project);

        return project.getMembers().stream().map(User::getId).collect(Collectors.toList());
    }

    //list member of project
    public List<UserDTO> getProjectMembers(String id) {
        Long projectId = utility.validateAndConvertId(id);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id" + projectId));

        return project.getMembers().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    //UPDATE MEMBER OF PROJECT
    public List<Long> removeMembersFromProject(String id, List<Long> membersId) {
        Long projectId = utility.validateAndConvertId(id);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        List<User> membersToRemove = membersId.stream()
                .map(userId -> userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + userId)))
                .collect(Collectors.toList());

        project.getMembers().removeAll(membersToRemove);
        projectRepository.save(project);

        return project.getMembers().stream().map(User::getId).collect(Collectors.toList());
    }

    //update member in project
    public List<Long> updateMembersInProject(String id, List<Long> membersId) {
        Long projectId = utility.validateAndConvertId(id);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        List<User> newMembersId = membersId.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with " + userId)))
                .collect(Collectors.toList());

        project.setMembers(newMembersId);
        projectRepository.save(project);

        return project.getMembers().stream().map(User::getId).collect(Collectors.toList());
    }
}
