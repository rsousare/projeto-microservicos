package com.example.ticket.dto;


import com.example.ticket.entity.Ticket;

public record ProjectTicketProgress(Project project, Ticket topTicket, double progressPercentage) {

    
}
