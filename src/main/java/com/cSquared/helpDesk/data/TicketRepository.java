package com.cSquared.helpDesk.data;

import com.cSquared.helpDesk.models.StatusLevel;
import com.cSquared.helpDesk.models.Ticket;
import com.cSquared.helpDesk.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    List<Ticket> findByStatusLevel(StatusLevel statusLevel);
}
