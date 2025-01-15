package com.example.Area.service;

import com.example.Area.dto.Project;
import com.example.Area.entity.Area;
import com.example.Area.repository.AreaRepository;
import jakarta.persistence.EntityNotFoundException;
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
class AreaServiceTest {

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AreaService areaService;

    @Test
    void getAllAreas() {
        List<Area> areas = new ArrayList<>();
        areas.add(new Area());
        areas.add(new Area());

        when(areaRepository.findAll()).thenReturn(areas);

        List<Area> areas1 = areaService.getAllAreas();

        assertEquals(2, areas1.size());
        verify(areaRepository, times(1)).findAll();
    }

    @Test
    void getAreaById() {
        Long id = 1L;
        Area area = new Area(id, "Area", "Description", 1L);
        when(areaRepository.findById(id)).thenReturn(Optional.of(area));

        Optional<Area> areaOptional = areaService.getAreaById(id);
        assertTrue(areaOptional.isPresent());
        assertEquals(area, areaOptional.get());
        verify(areaRepository, times(1)).findById(id);
    }

    @Test
    void createArea() {
        Area area = new Area();
        area.setName("Area");

        when(areaRepository.save(any(Area.class))).thenReturn(area);

        Area newArea = areaService.createArea(area);

        assertEquals(area, newArea);
        verify(areaRepository, times(1)).save(area);
    }

    @Test
    void updateArea() {
        Long id = 1L;
        Area updatedArea = new Area(id, "Area", "Description", 1L);
        when(areaRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
        areaService.updateArea(updatedArea);
        });

        String expectedMessage = "Area with Id " + id + " not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(areaRepository, never()).save(any(Area.class));
    }

    @Test
    void deleteArea() {
        Long id = 1L;
        areaService.deleteArea(id);
        verify(areaRepository, times(1)).deleteById(id);
    }

    @Test
    void getAreasWithMostProjects() {
        Map<Long, Integer> projectCountByArea = new HashMap<>();
        projectCountByArea.put(1L, 5);
        projectCountByArea.put(2L, 3);
        when(restTemplate.exchange(
                "http://localhost:9092/projects/project-countAreas",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<Long, Integer>>() {}
        )).thenReturn(new ResponseEntity<>(projectCountByArea, HttpStatus.OK
        ));

        when(restTemplate.getForEntity(anyString(), eq(Area.class)))
                .thenAnswer(invocation ->{
                    String url = invocation.getArgument(0);
                    Long id = Long.parseLong(url.substring(url.lastIndexOf("/") + 1));

                    Area area = new Area();
                    area.setId(id);
                    area.setName("Area " + id);
                    area.setDescription("Description " + id);
                    return new ResponseEntity<>(area, HttpStatus.OK);
                });

        List<Area> result = areaService.getAreasWithMostProjects();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getProjectsForArea() {
        Project[] projects = new Project[2];
        projects[0] = new Project();
        projects[0].setId(1);
        //projects[0].setName("Project");
        projects[1] = new Project();
        projects[1].setId(2);
        //projects[1].setName("Project2");

        when(restTemplate.getForEntity(anyString(), eq(Project[].class)))
                .thenReturn(new ResponseEntity<>(projects, HttpStatus.OK));

        List<Project> result = areaService.getProjectsForArea(1L);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }
}