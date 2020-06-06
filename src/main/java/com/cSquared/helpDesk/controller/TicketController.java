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
import java.util.ArrayList;
import java.util.List;
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
            List<Ticket> allOpenTickets = new ArrayList<>();
            List<Ticket> allClosedTickets = new ArrayList<>();
            for(Ticket ticket : ticketRepository.findAll()) {
                if(ticket.getStatusLevel().equals(StatusLevel.CLOSED)) {
                    allClosedTickets.add(ticket);
                } else {
                    allOpenTickets.add(ticket);
                }
            }

            model.addAttribute("tickets", allOpenTickets);
            model.addAttribute("closedTickets", allClosedTickets);
            model.addAttribute("unassignedTickets", ticketRepository.findByStatusLevel(StatusLevel.UNASSIGNED));

        } else if(user.getAccessLevel().equals(AccessLevel.TECH)) {
            model.addAttribute("unassignedTickets", ticketRepository.findByStatusLevel(StatusLevel.UNASSIGNED));
            model.addAttribute("tickets", user.allOpenTickets());
            model.addAttribute("closedTickets", user.allClosedTickets());

        } else {
            model.addAttribute("tickets", user.allOpenTickets());
            model.addAttribute("closedTickets", user.allClosedTickets());
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
        return "redirect:/ticket/";
    }

    @GetMapping("view/{ticketId}")
    public String viewTicket (@PathVariable int ticketId, HttpSession session,
                              Model model){



        User user = authenticationController.getUserFromSession(session);
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);


        if(ticket.isEmpty() || (!ticket.get().getUserCreated().equals(user) && user.getAccessLevel().equals(AccessLevel.ADMIN))){
            return "redirect:/ticket/";
        }

        model.addAttribute("admin", AccessLevel.ADMIN);
        model.addAttribute("tech", AccessLevel.TECH);
        model.addAttribute("user", user);
        model.addAttribute("techUsers", userRepository.findAllByAccessLevel(AccessLevel.TECH));
        model.addAttribute("ticket", ticket.get());
        model.addAttribute("statusLevels", StatusLevel.values());
        return "ticket/view";
    }

    @PostMapping("view/{ticketId}")
    public String updateTicket (@PathVariable int ticketId,
                                @RequestParam(required = false) Boolean isClosed,
                                @RequestParam(required = false) Integer userId,
                                Model model) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if(ticket.isEmpty()) {
            return "ticket/index";
        }
        Ticket currentTicket = ticket.get();
        if(isClosed != null) {
            if(isClosed) {
                currentTicket.setStatusLevel(StatusLevel.CLOSED);
                ticketRepository.save(currentTicket);
            }
        }

        if(userId != null) {
            Optional<User> user = userRepository.findById(userId);

            if(user.isPresent()){
                currentTicket.setTechAssigned(user.get());
                currentTicket.setStatusLevel(StatusLevel.ACTIVE);
                ticketRepository.save(currentTicket);
            }
        }




        return "redirect:/ticket/view/" + ticketId;
    }
}
