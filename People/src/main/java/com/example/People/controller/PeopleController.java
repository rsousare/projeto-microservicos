package com.example.People.controller;


import com.example.People.dto.PeopleDTO;
import com.example.People.entity.People;
import com.example.People.service.PeopleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/people")
public class PeopleController {
    @Autowired
    private PeopleService peopleService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves all people.
     * @return A list of all people.
     */
    @GetMapping
    public ResponseEntity<?> getAllPeople(){
        try {
            List<People> people = peopleService.getAllPeople();
            List<PeopleDTO> peopleDTOS = people.stream()
                    .map(person -> modelMapper.map(person, PeopleDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(peopleDTOS);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Invalid Request" + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving people: " + e.getMessage());
        }
    }

    /**
     * Retrieves a person by their ID.
     * @param id The ID of the person to retrieve.
     * @return The person with the specified ID, or null if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPeopleById(@PathVariable Long id){
        return peopleService.getPeopleById(id);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<List<People>> getPeopleByName(@PathVariable String name){
        List<People> people = peopleService.getPeopleByName(name);
        return ResponseEntity.ok().body(people);
    }

    /**
     * Creates a new person.
     * @param people The person data for creation.
     * @return The newly created person.
     */
    @PostMapping
    public People createPeople(@RequestBody People people){
        return peopleService.createPeople(people);
    }

    @PostMapping("/create")
    public String createPeopleHtml(@RequestParam ("name") String name, @RequestParam ("email") String email){
        People people = new People();
        people.setName(name);
        people.setEmail(email);
        peopleService.createPeople(people);
        return "Person created successfully";
    }

    /**
     * Updates an existing person.
     * @param people The updated person data.
     * @return The updated person.
     */
    @PutMapping("/{id}")
    public People updatePeople(@RequestBody People people){
        return peopleService.updatePeople(people);
    }

    /**
     * Deletes a person by their ID.
     * @param id The ID of the person to delete.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePeople(@PathVariable Long id){
        try {
            peopleService.deletePeople(id);
            return ResponseEntity.ok("Person deleted successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid person ID");
        }
    }
}
