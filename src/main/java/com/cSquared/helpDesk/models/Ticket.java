package com.cSquared.helpDesk.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
public class Ticket extends AbstractEntity {

    @NotBlank(message="Title is required.")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters.")
    private String title;

    private LocalDateTime dateCreated;
    private String dateCreatedString;

    @NotBlank(message = "Description is required.")
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters.")
    private String description;

    @ManyToOne
    private User userCreated;

    private SeverityLevel severity;

    private StatusLevel statusLevel;

    public Ticket(String title, String description, User userCreated, SeverityLevel severity) {
        this.title = title;
        this.description = description;
        this.userCreated = userCreated;
        this.dateCreated = LocalDateTime.now();
        this.dateCreatedString = dateCreated.format(DateTimeFormatter.ofPattern("MMM dd',' yyyy h:mm a"));
        this.severity = severity;
        this.statusLevel = StatusLevel.UNASSIGNED;
    }

    public Ticket() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

    public SeverityLevel getSeverity() {
        return severity;
    }

    public void setSeverity(SeverityLevel severity) {
        this.severity = severity;
    }

    public StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public String getDateCreatedString() {
        return dateCreatedString;
    }

    public void setDateCreatedString(String dateCreatedString) {
        this.dateCreatedString = dateCreatedString;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "title='" + title + '\'' +
                ", dateCreated=" + dateCreated +
                ", description='" + description + '\'' +
                ", userCreated=" + userCreated +
                '}';
    }


}
