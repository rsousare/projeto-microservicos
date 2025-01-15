package com.example.ticket.entity;

import com.example.ticket.dto.People;
import com.example.ticket.dto.Project;
import com.example.ticket.enums.TicketPriority;
import com.example.ticket.enums.TicketStatus;
import com.example.ticket.enums.TicketType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long id;

    @NotBlank(message = "The title cannot be empty")
    @Size(max = 50, message = "Max 50 characters")
    @Column(length = 50, nullable = false)
    private String title;

    @NotBlank(message = "The title cannot be empty")
    @Size(max = 200, message = "Max 200 characters")
    @Column(length = 200, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TicketType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TicketPriority priority;

    @Column(nullable = false)
    private int progress;

    @Column(nullable = false)
    private int estimate;

    @CreationTimestamp
    @Column(name = "created_At", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime resolvedAt;

    private Long projectId;

    private Long peopleId;

    public void setProject(Project project){
        this.projectId = project.getId();
    }

    public void setAssignedTo(People people){
        this.peopleId = people.getId();
    }
}
