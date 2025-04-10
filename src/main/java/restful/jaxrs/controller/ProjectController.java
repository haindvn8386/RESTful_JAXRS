package restful.jaxrs.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restful.jaxrs.dto.ProjectDTO;
import restful.jaxrs.dto.StaffDTO;
import restful.jaxrs.dto.UpdateProjectDTO;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.service.ProjectService;
import restful.jaxrs.util.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")

public class ProjectController {
    @Autowired
    private ProjectService projectService;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProject(@PathVariable String id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Projects retrieved successfully", project));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getAllProjects() {
        List<ProjectDTO> project = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success(200, "Project retrieved successfully", project));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO created = projectService.createProject(projectDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED.value(), "Project created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(@PathVariable String id, @Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(ApiResponse.success(200, "Project Updated successfully", updatedProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success(200,"Project deleted successfully", id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> patchProject(@PathVariable String id, @Valid @RequestBody UpdateProjectDTO updateProjectDTO) {
        ProjectDTO updatedProject = projectService.patchProject(id, updateProjectDTO);
        return ResponseEntity.ok(ApiResponse.success(200, "Edit project successfully", updatedProject));
    }

    /// //////////////////////
    /// //////////////////////MEMBER
    /// //////////////////////
    //add member to project
    @PostMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<Long>>> addMembersInProject(@PathVariable String id, @Valid @RequestBody List<Long> memberIds) {
        List<Long> updatedMemberIds = projectService.addMembersInProject(id, memberIds);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED.value(),"Added member to project successfully", updatedMemberIds));
    }

    //list member
    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<StaffDTO>>> getMembersInProject(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(200,"Member retrieved successfully", projectService.getProjectMembers(id)));
    }

    //update member of project
    @PutMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<Long>>> updateMembersInProject(@PathVariable String id, @Valid @RequestBody List<Long> memberIds) {
        List<Long> updatedMemberIds = projectService.updateMembersInProject(id, memberIds);
        return ResponseEntity.ok(ApiResponse.success(200,"Member updated successfully",updatedMemberIds));
    }

    //delete member of project
    @DeleteMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<Long>>> removeMembersFromProject(@PathVariable String id, @Valid @RequestBody List<Long> membersId) {
        List<Long> updatedMemberIds = projectService.removeMembersFromProject(id, membersId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.ACCEPTED.value(),"Member deleted from project successfully", updatedMemberIds));
    }

}
