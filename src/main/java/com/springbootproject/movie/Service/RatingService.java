package com.springbootproject.movie.Service;

import com.springbootproject.movie.Model.Ratings;

import java.util.List;

public interface RatingService {
    Ratings createRatingWithMovieIdAndUserId(int movieId, int userId, Ratings newRating);
    Ratings updateTheRatingWithMovieAndUserId(int movieId, int userId, Ratings updatedRating);
    void deleteTheRatingWithMovieAndUserId(int movieId, int userId);
    List<Ratings> getAllReviewForTheUserID(int userId);
    List<Ratings> getAllReviewForTheMovieID(int movieId);
}
