package restful.jaxrs.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restful.jaxrs.dto.ProjectDTO;
import restful.jaxrs.dto.StaffDTO;
import restful.jaxrs.dto.UpdateProjectDTO;
import restful.jaxrs.entity.Project;
import restful.jaxrs.entity.Staff;
import restful.jaxrs.exception.DuplicateResourceException;
import restful.jaxrs.exception.ResourceNotFoundException;
import restful.jaxrs.mapper.ProjectMapper;
import restful.jaxrs.mapper.StaffMapper;
import restful.jaxrs.repository.ProjectRepository;
import restful.jaxrs.repository.StaffRepository;
import restful.jaxrs.util.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Data
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffMapper staffMapper;


    Map<String, String> errors;

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

        errors = new HashMap<>();
        if (projectRepository.existsByName(projectDTO.getName())) {
            errors.put("name", "Project with name '" + projectDTO.getName() + "' already exists");
            // throw new DuplicateResourceException("Project with name '" + projectDTO.getName() + "' already exists");
        }
        if (projectRepository.existsByCode(projectDTO.getCode())) {
            errors.put("code", "Project with code '" + projectDTO.getCode() + "' already exists");
        }

        // Nếu có lỗi, ném exception với tất cả lỗi
        if (!errors.isEmpty()) {
            throw new DuplicateResourceException(errors);
        }


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
        project.setStatusProject(projectDTO.getStatusProject());

        //update manager
        if (projectDTO.getManager() != null && projectDTO.getManager().getId() != null) {
            Staff staffManager = staffRepository.findById(projectDTO.getManager().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + projectDTO.getManager().getId()));
            project.setStaffManager(staffManager);
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
        if (updateProjectDTO.getCode() != null) {
            existingProject.setCode(updateProjectDTO.getCode());
        }
        if (updateProjectDTO.getName() != null) {
            existingProject.setName(updateProjectDTO.getName());
        }
        if (updateProjectDTO.getDescription() != null) {
            existingProject.setDescription(updateProjectDTO.getDescription());
        }
        if (updateProjectDTO.getStatusProject() != null) {
            existingProject.setStatusProject(updateProjectDTO.getStatusProject());
        }
        if (updateProjectDTO.getManager() != null) {
            Staff manager = staffRepository.findById(updateProjectDTO.getManager().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + updateProjectDTO.getManager().getId()));
            existingProject.setStaffManager(manager);
        }
        Project updatedProject = projectRepository.save(existingProject);
        return projectMapper.toDTO(updatedProject);
    }

    /// //////////////////////
    /// //////////////////////MEMBER SERVICE
    /// //////////////////////
    //add member to project
    public List<Long> addMembersInProject(String id, List<Long> staffsId) {
        Long projectId = utility.validateAndConvertId(id);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        List<Staff> staff = staffsId.stream()
                .map(staffId -> staffRepository.findById(staffId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + staffId)))
                .collect(Collectors.toList());

        project.setMembers(staff);
        projectRepository.save(project);

        return project.getMembers().stream().map(Staff::getId).collect(Collectors.toList());
    }

    //list member of project
    public List<StaffDTO> getProjectMembers(String id) {
        Long projectId = utility.validateAndConvertId(id);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id" + projectId));

        return project.getMembers().stream().map(staffMapper::toDto).collect(Collectors.toList());
    }

    //UPDATE MEMBER OF PROJECT
    public List<Long> removeMembersFromProject(String id, List<Long> membersId) {
        Long projectId = utility.validateAndConvertId(id);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        List<Staff> membersToRemove = membersId.stream()
                .map(staffId -> staffRepository.findById(staffId).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + staffId)))
                .collect(Collectors.toList());

        project.getMembers().removeAll(membersToRemove);
        projectRepository.save(project);

        return project.getMembers().stream().map(Staff::getId).collect(Collectors.toList());
    }

    //update member in project
    public List<Long> updateMembersInProject(String id, List<Long> membersId) {
        Long projectId = utility.validateAndConvertId(id);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        List<Staff> newMembersId = membersId.stream()
                .map(staffId -> staffRepository.findById(staffId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with " + staffId)))
                .collect(Collectors.toList());

        project.setMembers(newMembersId);
        projectRepository.save(project);

        return project.getMembers().stream().map(Staff::getId).collect(Collectors.toList());
    }
}
