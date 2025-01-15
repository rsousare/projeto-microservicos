package com.example.Project.service;

import com.example.Project.dto.Area;
import com.example.Project.dto.ProjectDTO;
import com.example.Project.entity.Project;
import com.example.Project.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getProjectById() {
        Long id = 1L;
        Project project = new Project();
        project.setId(id);
        project.setName("Test Person");

        Optional<Project> projectOptional = Optional.of(project);
        when(projectRepository.findById(id)).thenReturn(projectOptional);

        Area area = new Area();
        area.setId(1);
        area.setName("Test Area");
        area.setDescription("Test Description");

        ResponseEntity<Area> areaResponseEntity = ResponseEntity.ok(area);
        when(restTemplate.getForObject("http://AREA/areas/" + project.getAreaId(), Area.class)).thenReturn(areaResponseEntity.getBody());

        ResponseEntity<?> responseEntity = projectService.getProjectById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ProjectDTO);
        assertEquals(project.getId(), ((ProjectDTO) responseEntity.getBody()).getId());
        assertEquals(project.getName(), ((ProjectDTO) responseEntity.getBody()).getName());
    }

    @Test
    void getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        projectList.add(new Project());
        projectList.add(new Project());
        when(projectRepository.findAll()).thenReturn(projectList);

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void createProject() {
        Project project = new Project(1L, "Project Name", "02/02/2024", "27/02/2024", 1L);

        when(projectRepository.save(project)).thenReturn(project);
        Project createdProject = projectService.createProject(project);

        assertNotNull(createdProject);
        assertEquals(project.getName(), createdProject.getName());
    }

    @Test
    void updateProject() {
        Long projectId = 1L;
        Project existingProject = new Project(projectId, "Project Name", "02/02/2024", "27/02/2024", 1L);
        Project updatedProject = new Project(projectId, "Project Name1", "02/03/2024", "27/03/2024", 1L);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Project result = projectService.updateProject(updatedProject);

        assertNotNull(result);
        assertEquals(updatedProject.getName(), result.getName());
    }

    @Test
    void deleteProject() {
        Long projectId = 1L;
        doNothing().when(projectRepository).deleteById(projectId);

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    void getProjectsWithMostContributors() {
        Map<Long, Integer> ticketCountByProject = new HashMap<>();
        ticketCountByProject.put(1L, 5);
        ticketCountByProject.put(2L, 3);
        ResponseEntity<Map<Long, Integer>> ticketCountResponse = ResponseEntity.ok(ticketCountByProject);
        when(restTemplate.exchange(
                "http://localhost:9093/tickets/project-count",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<Long, Integer>>() {}
        )).thenReturn(ticketCountResponse);

        Project project1 = new Project(1L, "Project 1", "10/12/2022", "14/02/2023", 1L);
        Project project2 = new Project(2L, "Project 2", "15/10/2021", "05/05/2022", 2L);
        ResponseEntity<Project> projectResponse1 = ResponseEntity.ok(project1);
        ResponseEntity<Project> projectResponse2 = ResponseEntity.ok(project2);
        when(restTemplate.getForEntity(
                "http://localhost:9092/projects/1",
                Project.class
        )).thenReturn(projectResponse1);
        when(restTemplate.getForEntity(
                "http://localhost:9092/projects/2",
                Project.class
        )).thenReturn(projectResponse2);

        List<Project> result = projectService.getProjectsWithMostContributors();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }
}