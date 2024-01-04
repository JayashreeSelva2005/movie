package com.springbootproject.movie.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.util.List;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "unique_movie_name"))
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    private String description;

    @NotNull(message = "Status cannot be null")
    private Boolean status;


    @OneToMany(mappedBy = "movie")
    private List<CrewMember> crewMemberList;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private List<Ratings> ratings;

    public Movie() {

    }

    public Movie(String name, String description, List<CrewMember> crewMemberList, boolean status, List<Ratings> ratings) {
        this.name = name;
        this.description = description;
        this.crewMemberList = crewMemberList;
        this.status = status;
        this.ratings = ratings;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CrewMember> getCrewMemberList() {
        return crewMemberList;
    }

    public void setCrewMemberList(List<CrewMember> crewMemberList) {
        this.crewMemberList = crewMemberList;
    }
}

