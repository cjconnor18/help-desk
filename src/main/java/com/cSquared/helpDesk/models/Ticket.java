package com.cSquared.helpDesk.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Ticket extends AbstractEntity {

    @NotBlank(message="Title is required.")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters.")
    private String title;

    private Date dateCreated;

    @NotBlank(message = "Description is required.")
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters.")
    private String description;

    @ManyToOne
    private User userCreated;

    public Ticket(String title, String description, User userCreated) {
        this.title = title;
        this.description = description;
        this.userCreated = userCreated;
        this.dateCreated = new Date();
    }

    public Ticket() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
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
