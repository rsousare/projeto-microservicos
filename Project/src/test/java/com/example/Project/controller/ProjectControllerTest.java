package com.example.Project.controller;

import com.example.Project.dto.Area;
import com.example.Project.dto.ProjectDTO;
import com.example.Project.entity.Project;
import com.example.Project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProjectController projectController;


    @Test
    void getAllProjects() {
        when(modelMapper.map(any(), eq(ProjectDTO.class))).thenAnswer(invocationOnMock -> {
            Project source = invocationOnMock.getArgument(0);
            return new ProjectDTO(source.getId(), source.getName(), source.getStartDate(), source.getEndDate(), new Area());
        });

        Project project1 = new Project(1L, "Project1", "02/02/2024", "27/02/2024", 1L);
        Project project2 = new Project(2L, "Project2", "02/02/2024", "27/02/2024", 1L);

        List<Project> mockProjects = Arrays.asList(project1, project2);
        when(projectService.getAllProjects()).thenReturn(mockProjects);

        List<ProjectDTO> allProjects = projectController.getAllProjects();
        verify(projectService).getAllProjects();

        assertEquals(2, allProjects.size());
        assertEquals("Project1", allProjects.get(0).getName());
    }

    @Test
    void getProjectById() {
    }

    @Test
    void getProjectDetails() {

    }

    @Test
    void createProject() {
        Project projectToCreate = new Project();
        Project createdProject = new Project(1L, "New project", "02/02/2024", "26/02/2024", 1L);
        when(projectService.createProject(projectToCreate)).thenReturn(createdProject);

        Project returnedProject = projectController.createProject(projectToCreate);

        assertEquals(createdProject, returnedProject);
        verify(projectService).createProject(projectToCreate);
    }

    @Test
    void updateProject() {
        Long id = 1L;
        Project existingProject = mock(Project.class);
        when(projectService.updateProject(existingProject)).thenReturn(existingProject);

        Project updatedProject = projectController.updateProject(existingProject);
        assertEquals(existingProject, updatedProject);
        verify(projectService).updateProject(existingProject);
    }

    @Test
    void deleteProject() {
        Long id = 1L;
        projectController.deleteProject(id);
        verify(projectService).deleteProject(id);
    }

    @Test
    void getProjectsWithMostContributors() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project");

        when(projectService.getProjectsWithMostContributors()).thenReturn(List.of(project));
        ResponseEntity<List<Project>> result = projectController.getProjectsWithMostContributors();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(project.getId(), result.getBody().get(0).getId());
        assertEquals(project.getName(), result.getBody().get(0).getName());
    }

    @Test
    void getProjectCountByArea() {
        Map<Long, Integer> projectCountByArea = new HashMap<>();
        projectCountByArea.put(1L, 10);
        when(projectService.getProjectCountByArea()).thenReturn(projectCountByArea);

        ResponseEntity<Map<Long, Integer>> result = projectController.getProjectCountByArea();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(projectCountByArea, result.getBody());
    }
}