package com.springbootproject.movie.DTO;

import java.util.List;

public class UserDTO {

    private int id;
    private String name;
    private String emailId;

    public UserDTO() {
    }

    public UserDTO(int id, String name, String emailId) {
        this.id = id;
        this.name = name;
        this.emailId = emailId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
