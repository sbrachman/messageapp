package com.messageapp.service.dto;

import com.messageapp.domain.User;

import java.time.Instant;

public class UserQueryDTO {

    private String login;
    private String firstName;
    private String lastName;
    private Instant createdDate;

    public UserQueryDTO() {
    }

    public UserQueryDTO(User user) {
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.createdDate = user.getCreatedDate();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
