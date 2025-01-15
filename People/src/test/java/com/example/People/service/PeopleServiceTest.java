package com.example.People.service;

import com.example.People.dto.Area;
import com.example.People.dto.PeopleDTO;
import com.example.People.entity.People;
import com.example.People.reporitory.PeopleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private PeopleService peopleService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getAllPeople() {
        List<People> mockPeopleList = new ArrayList<>();
        mockPeopleList.add(new People());
        mockPeopleList.add(new People());
        when(peopleRepository.findAll()).thenReturn(mockPeopleList);

        List<People> result = peopleService.getAllPeople();

        assertEquals(2, result.size());
        verify(peopleRepository, times(1)).findAll();
    }

    @Test
    void getPeopleById() {
        Long id = 1L;
        People people = new People();
        people.setId(id);
        people.setName("Test Person");

        Optional<People> peopleOptional = Optional.of(people);
        when(peopleRepository.findById(id)).thenReturn(peopleOptional);

        Area area = new Area();
        area.setId(1);
        area.setName("Test Area");
        area.setDescription("Test Description");

        ResponseEntity<Area> areaResponseEntity = ResponseEntity.ok(area);
        when(restTemplate.getForObject("http://AREA/areas/" + people.getAreaId(), Area.class)).thenReturn(areaResponseEntity.getBody());

        ResponseEntity<?> responseEntity = peopleService.getPeopleById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof PeopleDTO);
        assertEquals(people.getId(), ((PeopleDTO) responseEntity.getBody()).getId());
        assertEquals(people.getName(), ((PeopleDTO) responseEntity.getBody()).getName());
    }

    @Test
    void getPeopleByName() {
        String name = "Vasco";
        List<People> expectedPeople = Arrays.asList(new People(1L, name, "vasco@example.com", "vv", "123", 1));
        when(peopleRepository.findByName(name)).thenReturn(expectedPeople);

        List<People> result = peopleService.getPeopleByName(name);

        assertEquals(expectedPeople, result);
    }

    @Test
    void createPeople() {
        People personToCreate = new People();
        personToCreate.setName("Vasco");
        personToCreate.setEmail("vasco@example.com");

        when(peopleRepository.save(personToCreate)).thenReturn(personToCreate);

        People result = peopleService.createPeople(personToCreate);

        assertEquals(personToCreate, result);
    }

    @Test
    void updatePeople() {
        Long id = 1L;
        People updatedPersonData = new People();
        updatedPersonData.setId(id);

        when(peopleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> peopleService.updatePeople(updatedPersonData));
    }

    @Test
    void deletePeople() {
        Long id = 1L;
        when(peopleRepository.findById(id)).thenReturn(Optional.of(new People()));

        peopleService.deletePeople(id);

        verify(peopleRepository, times(1)).deleteById(id);
    }
}