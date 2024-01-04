package com.springbootproject.movie.controller;

import com.springbootproject.movie.Model.Ratings;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.Service.RatingService;
import com.springbootproject.movie.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class RatingsController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/ratings/create/{movieId}")
    public ResponseEntity<Ratings> createRatingWithMovieIdAndUserId(@PathVariable int movieId, @Valid @RequestBody Ratings newRating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> userOptional =userRepository.findByEmail(userEmail);
        User user1 = userOptional.get();
        Ratings createdRating = ratingService.createRatingWithMovieIdAndUserId(movieId, user1.getId(), newRating);
        return ResponseEntity.ok(createdRating);
    }

    @PutMapping("/ratings/update/{movieId}")
    public ResponseEntity<Ratings> updateTheRatingWithMovieAndUserId(@PathVariable int movieId,  @Valid @RequestBody Ratings updatedRating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> userOptional =userRepository.findByEmail(userEmail);
        User user1 = userOptional.get();
        Ratings updated = ratingService.updateTheRatingWithMovieAndUserId(movieId, user1.getId(), updatedRating);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/ratings/delete/{movieId}")
    public ResponseEntity<String> deleteTheRatingWithMovieAndUserId(@PathVariable int movieId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> userOptional =userRepository.findByEmail(userEmail);
        User user1 = userOptional.get();
        ratingService.deleteTheRatingWithMovieAndUserId(movieId, user1.getId());
        return ResponseEntity.ok("Rating deleted successfully");
    }

    @GetMapping("/ratings/user/{userId}")
    public ResponseEntity<List<Ratings>> getAllReviewForTheUserID(@PathVariable int userId) {
        List<Ratings> reviews = ratingService.getAllReviewForTheUserID(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/ratings/movies/{movieId}")
    public ResponseEntity<List<Ratings>> getAllReviewForTheMovieID(@PathVariable int movieId) {
        List<Ratings> reviews = ratingService.getAllReviewForTheMovieID(movieId);
        return ResponseEntity.ok(reviews);
    }

}
