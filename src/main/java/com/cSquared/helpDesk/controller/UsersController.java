package com.cSquared.helpDesk.controller;

import com.cSquared.helpDesk.data.UserRepository;
import com.cSquared.helpDesk.models.AccessLevel;
import com.cSquared.helpDesk.models.Ticket;
import com.cSquared.helpDesk.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("users")
public class UsersController {
    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String displayUsers(Model model, HttpSession session) {
        User user = authenticationController.getUserFromSession(session);

        if(user.getAccessLevel() != AccessLevel.ADMIN) {
            List<User> users = new ArrayList<>();
            users.add(user);
            model.addAttribute("users", users);
        } else {
            model.addAttribute("users", userRepository.findAll());
        }
        return "users/index";
    }

    @GetMapping("view/{userId}")
    public String viewUser(@PathVariable int userId,
                              HttpSession session,
                              Model model){
        User currentUser = authenticationController.getUserFromSession(session);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return "users/index";
        }
        if(currentUser.getAccessLevel() == AccessLevel.USER) {
            if(currentUser.getId() != user.get().getId()) {
                return "users/index";
            } else {
                model.addAttribute("user", currentUser);
            }
        } else {
            model.addAttribute("user", user.get());
        }

        Boolean isAdmin = currentUser.isAdmin();
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("levels", AccessLevel.values());
        return "users/view";
    }

    @PostMapping("view/{userId}")
    public String changeAccessLevel(@PathVariable int userId, @RequestParam AccessLevel accessLevel){

        Optional<User> optUser = userRepository.findById(userId);
        if(optUser.isEmpty()) {
            return "view/";
        }
        User user = optUser.get();
        user.setAccessLevel(accessLevel);
        userRepository.save(user);
        return "redirect:/users/view/" + userId;
    }



}