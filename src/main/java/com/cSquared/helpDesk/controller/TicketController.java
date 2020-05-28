package com.cSquared.helpDesk.controller;

import com.cSquared.helpDesk.data.TicketRepository;
import com.cSquared.helpDesk.data.UserRepository;
import com.cSquared.helpDesk.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Optional;

@RequestMapping("ticket")
@Controller
public class TicketController {
    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @GetMapping
    public String displayTicketHome(Model model, HttpSession session){
        User user = authenticationController.getUserFromSession(session);

        if(user.getAccessLevel().equals(AccessLevel.ADMIN)) {
            model.addAttribute("tickets", ticketRepository.findAll());
        } else {
            model.addAttribute("tickets", user.getTickets());
        }

        model.addAttribute("user", user);
        return "ticket/index";
    }

    @GetMapping("create")
    public String displayCreateForm(Model model) {
        model.addAttribute(new Ticket());
        model.addAttribute("types", SeverityLevel.values());
        return "ticket/create";
    }

    @PostMapping("create")
    public String processCreateForm(@ModelAttribute @Valid Ticket ticket,
                                    Errors errors, HttpServletRequest request,
                                    HttpSession session, Model model) {
        if(errors.hasErrors()) {
            return "ticket/create";
        }
        User user = authenticationController.getUserFromSession(session);
        Ticket newTicket = new Ticket(ticket.getTitle(), ticket.getDescription(), user, ticket.getSeverity());
        ticketRepository.save(newTicket);
        return "redirect:";
    }

    @GetMapping("view/{ticketId}")
    public String viewTicket (@PathVariable int ticketId, HttpSession session,
                              Model model){



        User user = authenticationController.getUserFromSession(session);
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);


        if(ticket.isEmpty() || !ticket.get().getUserCreated().equals(user)){
            return "ticket";
        }

        model.addAttribute("ticket", ticket.get());
        model.addAttribute("statusLevels", StatusLevel.values());
        return "ticket/view";
    }

    @PostMapping("view/{ticketId}")
    public String updateTicket (@PathVariable int ticketId, @RequestParam StatusLevel statusLevelChosen, Model model) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if(ticket.isEmpty()) {
            return "index";
        }
        Ticket currentTicket = ticket.get();
        currentTicket.setStatusLevel(statusLevelChosen);
        ticketRepository.save(currentTicket);

        return "redirect:/ticket/view/" + ticketId;

    }
}
