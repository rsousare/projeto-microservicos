package com.example.ticket.service;

import com.example.ticket.dto.People;
import com.example.ticket.dto.Project;
import com.example.ticket.dto.TicketDTO;
import com.example.ticket.entity.Ticket;
import com.example.ticket.enums.TicketPriority;
import com.example.ticket.enums.TicketStatus;
import com.example.ticket.enums.TicketType;
import com.example.ticket.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        tickets.add(new Ticket());

        when(ticketRepository.findAll()).thenReturn(tickets);

        List<Ticket> allTickets = ticketService.getAllTickets();

        assertEquals(2, allTickets.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void getTicketById() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setTitle("Test Ticket");
        ticket.setDescription("Test Description");
        ticket.setStatus(TicketStatus.NEW);
        ticket.setType(TicketType.BUG);
        ticket.setPriority(TicketPriority.LOW);
        ticket.setProgress(0);
        ticket.setEstimate(10);
        ticket.setProjectId(1L);
        ticket.setPeopleId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        People people = new People();
        people.setId(1L);
        people.setName("Test Person");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        when(restTemplate.getForObject("http://PROJECT/projects/" + ticket.getProjectId(), Project.class))
                .thenReturn(project);

        when(restTemplate.getForObject("http://PEOPLE/people/" + ticket.getPeopleId(), People.class))
                .thenReturn(people);

        ResponseEntity<?> responseEntity = ticketService.getTicketById(ticketId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        TicketDTO ticketDTO = (TicketDTO) responseEntity.getBody();

        assertNotNull(ticketDTO);
        assertEquals(ticket.getId(), ticketDTO.getId());
        assertEquals(ticket.getTitle(), ticketDTO.getTitle());
        assertEquals(ticket.getDescription(), ticketDTO.getDescription());
        assertEquals(ticket.getStatus(), ticketDTO.getStatus());
        assertEquals(ticket.getType(), ticketDTO.getType());
        assertEquals(ticket.getPriority(), ticketDTO.getPriority());
        assertEquals(ticket.getProgress(), ticketDTO.getProgress());
        assertEquals(ticket.getEstimate(), ticketDTO.getEstimate());
        assertEquals(project.getId(), ticketDTO.getProject().getId());
        assertEquals(project.getName(), ticketDTO.getProject().getName());
        assertEquals(people.getId(), ticketDTO.getPeople().getId());
        assertEquals(people.getName(), ticketDTO.getPeople().getName());
    }

    @Test
    void createTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setDescription("Test Description");
        ticket.setProjectId(1L);
        ticket.setPeopleId(1L);

        Project project = new Project();
        People assignedPerson = new People();

        ResponseEntity<Project> projectResponse = new ResponseEntity<>(project, HttpStatus.OK);
        when(restTemplate.exchange(eq("http://localhost:9092/projects/1"), eq(HttpMethod.GET), any(), eq(Project.class)))
                .thenReturn(projectResponse);

        ResponseEntity<People> peopleResponse = new ResponseEntity<>(assignedPerson, HttpStatus.OK);
        when(restTemplate.exchange(eq("http://localhost:9094/people/1"), eq(HttpMethod.GET), any(), eq(People.class)))
                .thenReturn(peopleResponse);

        when(ticketRepository.save(ticket)).thenReturn(ticket);

        Ticket createdTicket = ticketService.createTicket(ticket);

        verify(ticketRepository, times(1)).save(ticket);

        assertEquals(ticket, createdTicket);
    }

    @Test
    void updateTicket() {
        Long id = 1L;
        Ticket updatedTicket = new Ticket(id, "Title1", "Original Description", TicketStatus.NEW, TicketType.OTHER,
                TicketPriority.LOW, 0, 10, LocalDateTime.now(), null, 1L, 1L);
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            ticketService.updateTicket(updatedTicket);
        });

        String expectedMessage = "Ticket with id: " + id + " not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void deleteTicket() {
        Long id = 1L;
        ticketService.deleteTicket(id);
        verify(ticketRepository, times(1)).deleteById(id);
    }

    @Test
    void updatePriority() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void updateType() {
    }

    @Test
    void assignTicket() {
    }

    @Test
    void updateProgress() {
    }

    @Test
    void searchByPriority() {
    }

    @Test
    void searchByStatus() {
    }

    @Test
    void searchCompletedTickets() {
    }

    @Test
    void getTopProjectTicketsByProgress() {
    }

    @Test
    void getTicketCountByProject() {
    }
}