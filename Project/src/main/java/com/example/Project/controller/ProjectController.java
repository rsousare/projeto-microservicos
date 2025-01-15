package com.example.Project.controller;


import com.example.Project.dto.ProjectDTO;
import com.example.Project.entity.Project;
import com.example.Project.rec.ProjectDetails;
import com.example.Project.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves all projects.
     * @return A list of all projects.
     */
    @GetMapping
    public List<ProjectDTO> getAllProjects(){
        List<Project> projects = projectService.getAllProjects();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a project by its ID.
     * @param id The ID of the project to retrieve.
     * @return The project with the specified ID, or null if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id){
        return projectService.getProjectById(id);
    }


    /**
     * Retrieves detailed information about a project by its ID.
     * @param id The ID of the project to retrieve details for.
     * @return Detailed information about the project, or null if the project is not found.
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<?> getProjectDetails(@PathVariable Long id) {
        ResponseEntity<?> responseEntity = projectService.getProjectById(id);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            ProjectDTO projectDTO = (ProjectDTO) responseEntity.getBody();
            return new ResponseEntity<>(new ProjectDetails(projectDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new project.
     * @param project The project data for creation.
     * @return The newly created project.
     */
    @PostMapping
    public Project createProject(@RequestBody Project project){
        return projectService.createProject(project);
    }

    /**
     * Updates an existing project.
     * @param project The updated project data.
     * @return The updated project.
     */
    @PutMapping("/{id}")
    public Project updateProject(@RequestBody Project project){
        return projectService.updateProject(project);
    }

    /**
     * Deletes a project by its ID.
     * @param id The ID of the project to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }

    @GetMapping("/most-contributors")
    public ResponseEntity<List<Project>> getProjectsWithMostContributors(){
        List<Project> projects = projectService.getProjectsWithMostContributors();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("project-countAreas")
    public ResponseEntity<Map<Long, Integer>> getProjectCountByArea(){
        Map<Long, Integer> projectCountByArea = projectService.getProjectCountByArea();
        return ResponseEntity.ok(projectCountByArea);
    }
}
