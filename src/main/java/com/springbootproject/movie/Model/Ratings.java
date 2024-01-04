package com.springbootproject.movie.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Ratings {

    @Id
    @GeneratedValue
    private int id;

    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 10")
    private int score;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotNull(message = "Status cannot be null")
    private boolean status;


    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Ratings() {
    }

    public Ratings(int score, String description, Movie movie, User user , boolean status) {
        this.score = score;
        this.description = description;
        this.movie = movie;
        this.user = user;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isStatus()  {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

