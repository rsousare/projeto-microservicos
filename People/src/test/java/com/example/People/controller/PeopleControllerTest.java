package com.example.People.controller;

import com.example.People.dto.Area;
import com.example.People.dto.PeopleDTO;
import com.example.People.entity.People;
import com.example.People.service.PeopleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleControllerTest {

    @Mock
    private PeopleService peopleService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PeopleController peopleController;


    @Test
    void getAllPeople() {
        when(modelMapper.map(any(), eq(PeopleDTO.class))).thenAnswer(invocationOnMock -> {
            People source = invocationOnMock.getArgument(0);
            return new PeopleDTO(source.getId(), source.getName(), source.getEmail(),  new Area());
        });

        People people1 = new People(1L, "Ricardo", "aa@gmail.com", "ric", "123", 1);
        People people2 = new People(2L, "Vasco", "vv@gmail.com", "vac", "345", 2);

        List<People> peopleList = Arrays.asList(people1, people2);
        when(peopleService.getAllPeople()).thenReturn(peopleList);

        ResponseEntity<?> responseEntity = peopleController.getAllPeople();
        verify(peopleService).getAllPeople();

        List<PeopleDTO> allPeople = (List<PeopleDTO>) responseEntity.getBody();
        assertEquals(2, allPeople.size());
        assertEquals("Ricardo", allPeople.get(0).getName());
        assertEquals("Vasco", allPeople.get(1).getName());
    }

    @Test
    void getPeopleById() {

    }

    @Test
    void getPeopleByName() {
        String name = "Vasco";
        People person1 = new People(1L, name, "vasco@example.com", "jd", "123", 1);
        People person2 = new People(2L, name, "Jose@example.com", "jd", "123", 1);
        List<People> peopleList = Arrays.asList(person1, person2);

        when(peopleService.getPeopleByName(name)).thenReturn(peopleList);

        ResponseEntity<List<People>> result = peopleController.getPeopleByName(name);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertEquals(peopleList, result.getBody());

        verify(peopleService).getPeopleByName(name);
    }

    @Test
    void createPeople() {
        People person = new People(1L, "Vasco", "vasco@example.com", "jd", "123", 1);

        when(peopleService.createPeople(any())).thenReturn(person);

        People createdPerson = peopleController.createPeople(person);

        assertEquals(person, createdPerson);

        verify(peopleService).createPeople(person);
    }

    @Test
    void updatePeople() {
        People person = new People(1L, "Vasco", "vasco@example.com", "jd", "123", 1);

        when(peopleService.updatePeople(any())).thenReturn(person);

        People updatedPerson = peopleController.updatePeople(person);

        assertEquals(person, updatedPerson);

        verify(peopleService).updatePeople(person);
    }

    @Test
    void deletePeople() {
        Long id = 1L;

        doNothing().when(peopleService).deletePeople(id);

        ResponseEntity<String> result = peopleController.deletePeople(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertEquals("Person deleted successfully", result.getBody());

        verify(peopleService).deletePeople(id);
    }
}