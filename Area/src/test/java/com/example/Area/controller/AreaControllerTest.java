package com.example.Area.controller;

import com.example.Area.dto.AreaDTO;
import com.example.Area.dto.Project;
import com.example.Area.entity.Area;
import com.example.Area.service.AreaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AreaControllerTest {

    @Mock
    private AreaService areaService;

    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AreaController areaController;

    @Test
    void getAllAreas() {
        when(modelMapper.map(any(), eq(AreaDTO.class))).thenAnswer(invocationOnMock -> {
            Area source = invocationOnMock.getArgument(0);
            return new AreaDTO(source.getId(), source.getName(), source.getDescription(), new Project());
        });

        Area area1 = new Area(1L, "Area1", "AreaDes", 1L);
        Area area2 = new Area(2L, "Area2", "AreaDes", 1L);

        List<Area> mockAreas = Arrays.asList(area1, area2);
        when(areaService.getAllAreas()).thenReturn(mockAreas);

        List<AreaDTO> allAreas = areaController.getAllAreas();
        verify(areaService).getAllAreas();

        assertEquals(2, allAreas.size());
        assertEquals("Area1", allAreas.get(0).getName());
        assertEquals("AreaDes", allAreas.get(1).getDescription());
    }

    @Test
    void getAreaById() {
        Long id = 1L;
        Area mockArea = new Area();
        mockArea.setId(id);
        mockArea.setName("Test Area");
        mockArea.setDescription("Description");
        when(areaService.getAreaById(id)).thenReturn(Optional.of(mockArea));

        Optional<Area> areaById = areaController.getAreaById(id);

        assertEquals(mockArea.getId(), areaById.get().getId());
        verify(areaService).getAreaById(id);
    }

    @Test
    void createArea() {
        Area areaToCreate = new Area();
        Area createdArea = new Area(1L, "New Area", "Description", 1L);
        when(areaService.createArea(areaToCreate)).thenReturn(createdArea);

        Area returnedArea = areaController.createArea(areaToCreate);

        assertEquals(createdArea, returnedArea);
        verify(areaService).createArea(areaToCreate);
    }

    @Test
    void createAreaHtml() {
        String name = "Area";
        String description = "Description";

        String result = areaController.createAreaHtml(name, description);
        assertEquals("Area created successfully", result);
    }

    @Test
    void updateArea() {
        Area area = new Area();
        area.setId(1L);
        area.setName("Area");
        area.setDescription("Description");
        when(areaService.updateArea(area)).thenReturn(area);

        Area result = areaController.updateArea(area);
        assertEquals(area.getId(), result.getId());
        assertEquals(area.getName(), result.getName());
        assertEquals(area.getDescription(), result.getDescription());
    }

    @Test
    void deleteArea() {
        doNothing().when(areaService).deleteArea(1L);
        areaController.deleteArea(1L);
        verify(areaService, times(1)).deleteArea(1L);
    }

    @Test
    void getAreasWithMostProjects() {
        Area area = new Area();
        area.setId(1L);
        area.setName("Area");
        area.setDescription("Description");

        when(areaService.getAreasWithMostProjects()).thenReturn(List.of(area));
        ResponseEntity<List<Area>> result = areaController.getAreasWithMostProjects();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(area.getId(), result.getBody().get(0).getId());
        assertEquals(area.getName(), result.getBody().get(0).getName());
        assertEquals(area.getDescription(), result.getBody().get(0).getDescription());
    }
}