package com.springbootproject.movie.DTO;

import com.springbootproject.movie.Model.Movie;

import java.util.List;

public class MovieDTO {

    private int id;
    private String name;
    private String description;
    private List<CrewMemberDTO> crewMemberList;
    private List<RatingsDTO> ratings;

    // Constructors

    public MovieDTO() {
    }

    public MovieDTO(int id, String name, String description, List<CrewMemberDTO> crewMemberList, List<RatingsDTO> ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.crewMemberList = crewMemberList;
        this.ratings = ratings;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CrewMemberDTO> getCrewMemberList() {
        return crewMemberList;
    }

    public void setCrewMemberList(List<CrewMemberDTO> crewMemberList) {
        this.crewMemberList = crewMemberList;
    }

    public List<RatingsDTO> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingsDTO> ratings) {
        this.ratings = ratings;
    }


}
