package com.cSquared.helpDesk.controller;

import com.cSquared.helpDesk.data.UserRepository;
import com.cSquared.helpDesk.models.AccessLevel;
import com.cSquared.helpDesk.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
    public String viewTicket (@PathVariable int userId,
                              Errors errors,
                              HttpSession session,
                              Model model){
        User currentUser = authenticationController.getUserFromSession(session);
        if(currentUser.getAccessLevel() == AccessLevel.USER) {
            if(currentUser.getId() != userId) {
                errors.rejectValue("userAccess", "user.accessLevel", "User access denied. Please contact your administrator.");
                return "users/index";
            } else {
                model.addAttribute("user", currentUser);
            }
        } else {

            model.addAttribute("user", userRepository.findById(userId));
        }



        return "users/view";
    }



}
