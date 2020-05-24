package com.cSquared.helpDesk.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class UserProfile extends AbstractEntity{

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 20, message = "Please enter 2 to 20 Characters")
    private String firstName;

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 20, message = "Please enter 2 to 20 Characters")
    private String lastName;

    @NotBlank(message = "Email is Required.")
    @Email(message = "Invalid email. Try again.")
    private String email;

    @Column(length = 10)
    private String phoneNumber;



    public UserProfile(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public UserProfile() {}
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
