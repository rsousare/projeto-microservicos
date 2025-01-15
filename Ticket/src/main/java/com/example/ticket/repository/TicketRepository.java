package com.example.ticket.repository;


import com.example.ticket.entity.Ticket;
import com.example.ticket.enums.TicketPriority;
import com.example.ticket.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByPriority(TicketPriority priority);
    List<Ticket> findByStatus(TicketStatus status);

//    List<Ticket> findByCreatedByOrderByCreatedAtDesc(People createdBy);
//    List<Ticket> findByResolvedByOrderByResolvedAtDesc(People resolvedBy);



    @Query("SELECT t.projectId, COUNT(t.id) FROM Ticket t GROUP BY t.projectId")
    List<Object[]> findTicketCountByProject();
}
