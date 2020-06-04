package com.cSquared.helpDesk.controller;

import com.cSquared.helpDesk.data.UserRepository;
import com.cSquared.helpDesk.models.AccessLevel;
import com.cSquared.helpDesk.models.User;
import com.cSquared.helpDesk.models.UserProfile;
import com.cSquared.helpDesk.models.dto.LoginFormDTO;
import com.cSquared.helpDesk.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }
    @GetMapping
    public String displayPageAfterLogin() {
        return "index";
    }

    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute(new UserProfile());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid UserProfile userProfile,
                                          Errors error1,
                                          @ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {

        if(errors.hasErrors() || error1.hasErrors()) {
            return "register";
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());
        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "Please choose a different username.");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if(!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match.");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword(), userProfile);
        if(userRepository.findByAccessLevel(AccessLevel.ADMIN) == null){
            newUser.setAccessLevel(AccessLevel.ADMIN);
        }

        userRepository.save(newUser);

        return "redirect:login";
    }

    @GetMapping("login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        return "login";
    }

    @PostMapping("login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {
        if(errors.hasErrors()) {
            return "login";
        }

        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());
        if(theUser == null) {
            errors.rejectValue("username", "user.invalid", "Invalid username.");
            return "login";
        }

        String password = loginFormDTO.getPassword();
        if(!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password.");
            return "login";
        }

        setUserInSession(request.getSession(), theUser);


        return "redirect:";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:login";
    }

}
