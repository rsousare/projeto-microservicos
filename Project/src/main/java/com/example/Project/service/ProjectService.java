package com.example.Project.service;


import com.example.Project.dto.Area;
import com.example.Project.dto.ProjectDTO;
import com.example.Project.entity.Project;
import com.example.Project.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


/**
 * Service to handle operations related to projects.
 */
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * Retrieves a project by ID.
     * @param id The ID of the project to retrieve.
     * @return An Optional containing the project if found.
     */
    public ResponseEntity<?> getProjectById(Long id){
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()){
            Project project = projectOptional.get();
            Area area = restTemplate.getForObject("http://AREA/areas/" + project.getAreaId(), Area.class);
            ProjectDTO projectDTO = new ProjectDTO(
            project.getId(),
            project.getName(),
            project.getStartDate(),
            project.getEndDate(),
            area
         );
            return new ResponseEntity<>(projectDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("No project found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Retrieves all projects.
     * @return List of all projects.
     */
    public List<Project> getAllProjects(){
        if (projectRepository == null){
            throw new IllegalArgumentException("ProjectRepository cannot be null");
        }
        try {
            List<Project> projects = projectRepository.findAll();
            if (projects.isEmpty()) {
                throw new IllegalArgumentException("Project cannot be empty");
            }
            return projects;
        }catch (DataAccessException e){
            return Collections.emptyList();
        }
    }



    /**
     * Creates a new project.
     * @param project The project to be created.
     * @return The newly created project.
     */
    public Project createProject(Project project){
        if (project == null){
            throw new IllegalArgumentException("Project cannot be null");
        }
        if (project.getName() == null || project.getName().isEmpty()){
            throw new IllegalArgumentException("Project name is required");
        }
        return projectRepository.save(project);
    }

    /**
     * Updates an existing project.
     * @param updateProject The project with updated data.
     * @return The updated project.
     * @throws EntityNotFoundException If the project is not found.
     */
    public Project updateProject(Project updateProject){
        if (updateProject == null){
            throw new IllegalArgumentException("Update cannot be null");
        }
        Optional<Project> projectOptional = projectRepository.findById(updateProject.getId());

        if (projectOptional.isEmpty()){
            throw new EntityNotFoundException("Project with id " + updateProject.getId() + " not found!");
        }

            Project existingProject = projectOptional.get();
            existingProject.setName(updateProject.getName());
            existingProject.setStartDate(updateProject.getStartDate());
            existingProject.setEndDate(updateProject.getEndDate());

            return projectRepository.save(existingProject);
    }

    /**
     * Deletes a project by ID.
     * @param projectId The ID of the project to be deleted.
     */
    public void deleteProject(Long projectId){
        if (projectId == null || projectId <= 0){
            throw new IllegalArgumentException("Invalid id project");
        }
        projectRepository.deleteById(projectId);
    }

    /**
     * Retrieves projects with the most contributors.
     * @return List of projects with the most contributors.
     */

    public List<Project> getProjectsWithMostContributors() {
        ResponseEntity<Map<Long, Integer>> responseEntity = restTemplate.exchange(
                "http://localhost:9093/tickets/project-count",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<Long, Integer>>() {}
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<Long, Integer> ticketCountByProject = responseEntity.getBody();

            List<Project> sortedProjects = new ArrayList<>();
            ticketCountByProject.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> {
                        Long projectId = entry.getKey();
                        ResponseEntity<Project> projectResponseEntity = restTemplate.getForEntity(
                                "http://localhost:9092/projects/" + projectId,
                                Project.class
                        );
                        if (projectResponseEntity.getStatusCode() == HttpStatus.OK) {
                            Project project1 = projectResponseEntity.getBody();
                            sortedProjects.add(project1);
                        } else {
                            throw new RuntimeException("Failed to retrieve project details for project ID " + projectId);
                        }
                    });

            return sortedProjects;
        } else {
            throw new RuntimeException("Failed to retrieve ticket count by project: " + responseEntity.getStatusCode());
        }
    }


    public Map<Long, Integer> getProjectCountByArea(){
        List<Object[]> results = projectRepository.findProjectCountByArea();
        Map<Long, Integer> projectCountByArea = new HashMap<>();
        for (Object[] result : results){
            Long areaId =(Long)result[0];
            Integer projectCount = ((Number) result[1]).intValue();
            projectCountByArea.put(areaId, projectCount);
        }
            return projectCountByArea;
    }
}
