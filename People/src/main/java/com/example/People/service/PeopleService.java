package com.example.People.service;


import com.example.People.dto.Area;
import com.example.People.dto.PeopleDTO;
import com.example.People.entity.People;
import com.example.People.reporitory.PeopleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Service to handle operations related to people.
 */
@Service
public class PeopleService {
    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves all people.
     * @return List of all people.
     */
    public List<People> getAllPeople(){
        if (peopleRepository == null){
            throw new IllegalArgumentException("PeopleRepository cannot be null");
        }
        List<People> people;
        try {
            people = peopleRepository.findAll();
            if (people.isEmpty()) {
                throw new IllegalArgumentException("People cannot be empty");
            }
        }catch (DataAccessException e){
            return Collections.emptyList();
        }
        return people;
    }

    /**
     * Retrieves a person by ID.
     * @param id The ID of the person to retrieve.
     * @return An Optional containing the person if found.
     */
    public ResponseEntity<?> getPeopleById(Long id){
        Optional<People> peopleOptional = peopleRepository.findById(id);
        if (peopleOptional.isPresent()){
        People people = peopleOptional.get();
            Area area = restTemplate.getForObject("http://AREA/areas/" + people.getAreaId(), Area.class);
            PeopleDTO peopleDTO = new PeopleDTO(
                    people.getId(),
                    people.getName(),
                    people.getEmail(),
                    area
            );
            return new ResponseEntity<>(peopleDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("People not found", HttpStatus.NOT_FOUND);
        }
    }


    public List<People> getPeopleByName(String name){
        if (StringUtils.isEmpty(name)){
            throw new IllegalArgumentException("The name cannot be empty");
        }
        return peopleRepository.findByName(name);
    }

    /**
     * Creates a new person.
     * @param people The person to be created.
     * @return The newly created person.
     */
    public People createPeople(People people){
        if (people == null){
            throw new IllegalArgumentException("People cannot be null");
        }
        if (people.getName() == null || people.getName().isEmpty()){
            throw new IllegalArgumentException("People name is required");
        }
        return peopleRepository.save(people);
    }

    /**
     * Updates an existing person.
     * @param updatePeople The person with updated data.
     * @return The updated person.
     * @throws EntityNotFoundException If the person is not found.
     */
    public People updatePeople(People updatePeople){
        if (updatePeople == null){
            throw new IllegalArgumentException("Update cannot be null");
        }
        Optional<People> existingPeople = peopleRepository.findById(updatePeople.getId());

        if (existingPeople.isEmpty()) {
            throw new EntityNotFoundException("People with id " + updatePeople.getId() + " not found!");
        }
            People peopleToUpdate = existingPeople.get();
            peopleToUpdate.setName(updatePeople.getName());
            peopleToUpdate.setEmail(updatePeople.getEmail());

            return peopleRepository.save(peopleToUpdate);
    }

    /**
     * Deletes a person by ID.
     * @param id The ID of the person to be deleted.
     */
    public void deletePeople(Long id){
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Invalid people id");
        }
        Optional<People> peopleOptional = peopleRepository.findById(id);
        if (peopleOptional.isPresent()){
            peopleRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException("Person not found with the provided ID");
        }
    }
}
