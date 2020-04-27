package com.cSquared.helpDesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("ticket")
@Controller
public class Ticket {

    @GetMapping
    public String displayTicketHome(){
        return "ticket/index";
    }
}
