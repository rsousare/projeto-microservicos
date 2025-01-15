package com.example.Area.controller;


import com.example.Area.entity.Area;
import com.example.Area.dto.AreaDTO;
import com.example.Area.service.AreaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/areas")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves all areas.
     * @return A list of all areas.
     */
    @GetMapping
    public List<AreaDTO> getAllAreas(){
        List<Area> areas = areaService.getAllAreas();
        return areas.stream()
                .map(area -> modelMapper.map(area, AreaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an area by its ID.
     * @param id The ID of the area to retrieve.
     * @return The area with the specified ID, or null if not found.
     */
    @GetMapping("/{id}")
    public Optional<Area> getAreaById(@PathVariable Long id){
        return areaService.getAreaById(id);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getProjectsByAreaId(@PathVariable Long id){
//        ResponseEntity<?> response;
//        try {
//            List<Project> projects = areaService.getProjectsForArea(id);
//            if (!projects.isEmpty()){
//                return new ResponseEntity<>(projects, HttpStatus.OK);
//            }else {
//                response = new ResponseEntity<>("No projects found for Area id" + id, HttpStatus.NOT_FOUND);
//            }
//        }catch (Exception e){
//            response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return response;
//    }

    /**
     * Creates a new area.
     * @param area The area data for creation.
     * @return The newly created area.
     */
    @PostMapping
    public Area createArea(@RequestBody Area area){
        return areaService.createArea(area);
    }

    @PostMapping("/create")
    public String createAreaHtml(@RequestParam ("name") String name, @RequestParam ("description") String description){
        Area area = new Area();
        area.setName(name);
        area.setDescription(description);
        areaService.createArea(area);
        return "Area created successfully";
    }

    /**
     * Updates an existing area.
     * @param area The updated area data.
     * @return The updated area.
     */
    @PutMapping("/{id}")
    public Area updateArea(@RequestBody Area area){
        return areaService.updateArea(area);
    }

    /**
     * Deletes an area by its ID.
     * @param id The ID of the area to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteArea(@PathVariable Long id){
        areaService.deleteArea(id);
    }

    @GetMapping("/mostProjects")
    public ResponseEntity<List<Area>> getAreasWithMostProjects(){
        List<Area> areas = areaService.getAreasWithMostProjects();
        return new ResponseEntity<>(areas, HttpStatus.OK);
    }
}
