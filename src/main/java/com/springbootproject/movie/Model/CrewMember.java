package com.springbootproject.movie.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbootproject.movie.validation.ValidateRoleTypeOfACrewMember;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
@Entity
@Table(name = "crew_members")
public class CrewMember {

    @Id
    @GeneratedValue
    private int id;
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @NotBlank(message = "Role cannot be blank")
    @Size(max = 255, message = "Role must be at most 255 characters")
    @ValidateRoleTypeOfACrewMember
    private String role;


    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    private Movie movie;

    // Constructors

    public CrewMember() {
    }

    public CrewMember(String name, String role, Movie movie) {
        this.name = name;
        this.role = role;
        this.movie = movie;
    }


    // Getters and Setters

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

