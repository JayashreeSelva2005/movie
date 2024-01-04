package com.springbootproject.movie.DTO;

import java.util.List;

public class RatingsDTO {

    private long id;
    private int score;
    private String description;
    private Long movieId;
    private Long userId;


    public RatingsDTO() {
    }

    public RatingsDTO(long id, int score, String description, Long movieId, Long userId) {
        this.id = id;
        this.score = score;
        this.description = description;
        this.movieId = movieId;
        this.userId = userId;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



}

