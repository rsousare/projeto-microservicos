package com.example.ticket.dto;


import com.example.ticket.enums.TicketPriority;
import com.example.ticket.enums.TicketStatus;
import com.example.ticket.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketType type;
    private TicketPriority priority;
    private int progress;
    private int estimate;
    private Project project;
    private People people;
}
