package com.example.ticket.controller;

import com.example.ticket.dto.Project;
import com.example.ticket.entity.Ticket;
import com.example.ticket.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TicketController ticketController;


    @Test
    void getAllTickets() {
    }

    @Test
    void getTicketById() {
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
    void createTicket() {
        Ticket newTicket = new Ticket();
        newTicket.setTitle("New ticket");
        newTicket.setDescription("New description");
        newTicket.setPeopleId(1L);

        Project project = new Project();
        project.setId(1L);
        newTicket.setProject(project);

        when(ticketService.createTicket(any(Ticket.class))).thenReturn(newTicket);

        Ticket createdTicket = ticketController.createTicket(newTicket);

        assertEquals(newTicket, createdTicket);
        verify(ticketService, times(1)).createTicket(newTicket);
    }

    @Test
    void updateTicket() {
        Long id = 1L;
        Ticket existingTicket = mock(Ticket.class);

        Ticket updateTicket = new Ticket(existingTicket.getId(), existingTicket.getTitle(), existingTicket.getDescription(),
                existingTicket.getStatus(), existingTicket.getType(), existingTicket.getPriority(), existingTicket.getProgress(),
                existingTicket.getEstimate(), existingTicket.getResolvedAt(),
                existingTicket.getCreatedAt(), existingTicket.getProjectId(), existingTicket.getPeopleId());

        when(ticketService.updateTicket(updateTicket)).thenReturn(updateTicket);

        Ticket result = ticketController.updateTicket(updateTicket);

        assertNotNull(result);
        assertEquals(updateTicket, result);

        verify(ticketService).updateTicket(updateTicket);
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
    void deleteTicket() {
        doNothing().when(ticketService).deleteTicket(1L);
        ticketController.deleteTicket(1L);
        verify(ticketService, times(1)).deleteTicket(1L);
    }

    @Test
    void getTicketCountByProject() {
    }
}