package com.cSquared.helpDesk.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User extends AbstractEntity{

    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    private UserProfile userProfile;

    @OneToMany(mappedBy = "userCreated")
    private final List<Ticket> tickets = new ArrayList<>();

    private AccessLevel accessLevel;

    public User(String username, String password) {
        this.username = username;
        this.pwHash = encoder.encode(password);
    }

    public User(String username, String password, UserProfile userProfile) {
        this(username, password);
        this.userProfile = userProfile;
        this.accessLevel = AccessLevel.USER;
    }

    public User(String username, String password, UserProfile userProfile, AccessLevel accessLevel) {
        this(username, password, userProfile);
        this.accessLevel = accessLevel;
    }

    public User() {};

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Boolean isAdmin() {
        if(this.getAccessLevel().getDisplayName().equals("Administrator")) {
            return true;
        }
        return false;
    }
}
