package com.cSquared.helpDesk.controller;

import com.cSquared.helpDesk.data.UserRepository;
import com.cSquared.helpDesk.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
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

    private static void setUserSessionKey(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }
}
