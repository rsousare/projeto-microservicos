package com.example.Area.service;


import com.example.Area.dto.Project;
import com.example.Area.entity.Area;
import com.example.Area.repository.AreaRepository;
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
 * Service to handle operations related to areas.
 */
@Service
public class AreaService {
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves all areas.
     * @return List of all areas.
     */
    public List<Area> getAllAreas(){
        if (areaRepository == null){
            throw new IllegalArgumentException("Area cannot be null");
        }
        try {
            List<Area> areas = areaRepository.findAll();
            if (areas.isEmpty()) {
                throw new IllegalArgumentException("Areas cannot be empty");
            }
            return areas;
        }catch (DataAccessException e){
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves an area by ID.
     //* @param areaId The ID of the area to retrieve.
     * @return An Optional containing the area if found.
     */
    public Optional<Area> getAreaById(Long areaId){
        if (areaId == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        return areaRepository.findById(areaId);
    }


//    public AreaDT getAreaById(Long id){
//        Area area = areaRepository.findById(id).orElse(null);
//        ResponseEntity<Project[]> responseEntity = restTemplate.getForEntity("http://localhost:9092/projects?areaId=" + id, Project[].class);
//
//        if (area != null){
//            AreaDT areaDTO = new AreaDT();
//            areaDTO.setId(area.getId());
//            areaDTO.setName(area.getName());
//            areaDTO.setDescription(area.getDescription());
//            List<Project> projects = Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
//                    .map(project -> new Project(project.getId(), project.getName(), project.getStartDate(), project.getEndDate()))
//                    .collect(Collectors.toList());
//            areaDTO.setProjects(projects);
//            return areaDTO;
//        }
//        throw new RuntimeException("Failed to retrieve area with id " + id);
//    }

//    public ResponseEntity<?> getAreaById(Long id){
//        Optional<Area> areaOptional = areaRepository.findById(id);
//        if (areaOptional.isPresent()){
//            Area area = areaOptional.get();
//            Project[] projects = restTemplate.getForObject("http://localhost:9092/projects?areaId=" + area.getId(), Project[].class);
//            AreaDTO areaDTO = new AreaDTO(
//                    area.getId(),
//                    area.getName(),
//                    area.getDescription(),
//                    List.of(projects)
//            );
//            return new ResponseEntity<>(areaDTO, HttpStatus.OK);
//        }else {
//            return new ResponseEntity<>("Area not found", HttpStatus.NOT_FOUND);
//        }
//    }


    /**
     * Creates a new area.
     * @param area The area to be created.
     * @return The newly created area.
     */
    public Area createArea(Area area){
        if (area == null){
            throw new IllegalArgumentException("Area cannot be null");
        }
        if (area.getName() == null || area.getName().isEmpty()){
            throw new IllegalArgumentException("Area name is required");
        }
        return areaRepository.save(area);
    }

    /**
     * Updates an existing area.
     * @param newArea The new area with updated data.
     * @return The updated area.
     * @throws EntityNotFoundException If the area is not found.
     */

    public Area updateArea(Area newArea){
        if (newArea == null || newArea.getId() == 0){
            throw new IllegalArgumentException("The provided Area or Id cannot be null or 0.");
        }

        Optional<Area> areaOptional = areaRepository.findById(newArea.getId());
        if (areaOptional.isEmpty()){
            throw new EntityNotFoundException("Area with Id " + newArea.getId() + " not found");
        }

        Area existingArea  = areaOptional.get();

        if (newArea.getName() == null || newArea.getName().isEmpty()){
            throw new IllegalArgumentException("Area name cannot be null or empty");
        }
        existingArea.setName(newArea.getName());
        existingArea.setDescription(newArea.getDescription());

        return areaRepository.save(existingArea);
    }

    /**
     * Deletes an area by ID.
     * @param areaId The ID of the area to be deleted.
     */
    public void deleteArea(Long areaId){
        if (areaId == null || areaId <= 0){
            throw new IllegalArgumentException("Invalid id Area");
        }
        areaRepository.deleteById(areaId);
    }

    /**
     * Retrieves areas with the most contributors.
     * @return List of areas with the most contributors.
     */
    public List<Area> getAreasWithMostProjects(){
        ResponseEntity<Map<Long, Integer>> responseEntity = restTemplate.exchange(
                "http://localhost:9092/projects/project-countAreas",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<Long, Integer>>() {}
        );
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<Long, Integer> projectCountByArea = responseEntity.getBody();

            List<Area> sortedAreas = new ArrayList<>();
            projectCountByArea.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> {
                        Long areaId = entry.getKey();
                        ResponseEntity<Area> areaResponseEntity = restTemplate.getForEntity(
                                "http://localhost:9091/areas/" + areaId,
                                Area.class
                        );
                        if (areaResponseEntity.getStatusCode() == HttpStatus.OK) {
                            Area area1 = areaResponseEntity.getBody();
                            sortedAreas.add(area1);
                        } else {
                            throw new RuntimeException("Failed to retrieve project details for project ID " + areaId);
                        }
                    });

            return sortedAreas;
        } else {
            throw new RuntimeException("Failed to retrieve ticket count by project: " + responseEntity.getStatusCode());
        }
    }

    public List<Project> getProjectsForArea(Long id){
        String url = "http://localhost:9092/projects?areaId=" + id;
        ResponseEntity<Project[]> responseEntity = restTemplate.getForEntity(url, Project[].class);
        if (responseEntity.getStatusCode() == HttpStatus.OK){
            Project[] projects = responseEntity.getBody();
            return Arrays.asList(projects);
        }else {
            return Collections.emptyList();
        }
    }
}
