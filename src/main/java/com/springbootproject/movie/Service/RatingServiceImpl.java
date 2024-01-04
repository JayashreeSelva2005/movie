package com.springbootproject.movie.Service;

import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.Model.Ratings;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.repository.MovieRepository;
import com.springbootproject.movie.repository.RatingsRepository;
import com.springbootproject.movie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService{
    @Autowired
    private RatingsRepository ratingsRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Ratings createRatingWithMovieIdAndUserId(int movieId, int userId, Ratings newRating) {

        Optional<Ratings> existingRating = ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId,true);

        if (existingRating.isPresent()) {
            Ratings existing = existingRating.get();

            if (existing.isStatus()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already reviewed the movie");
            }
        }
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (movieOptional.isPresent() && userOptional.isPresent()) {
            Movie movie = movieOptional.get();
            User user = userOptional.get();

            if (movie.isStatus()) {
                newRating.setMovie(movie);
                newRating.setUser(user);
                newRating.setStatus(true);
                return ratingsRepository.save(newRating);
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot update an inactive movie");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot update because no movie or user");
        }
    }


    @Override
    public Ratings updateTheRatingWithMovieAndUserId(int movieId, int userId, Ratings updatedRating) {
        Optional<Ratings> existingRating = ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId,true);
        if (existingRating.isPresent()) {
            Ratings ratings = existingRating.get();

            if (ratings.getMovie().isStatus() && ratings.isStatus()) {
                ratings.setScore(updatedRating.getScore());
                ratings.setDescription(updatedRating.getDescription());

                return ratingsRepository.save(ratings);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update an inactive movie review");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update because no movie or user");
        }
    }



    @Override
    public void deleteTheRatingWithMovieAndUserId(int movieId, int userId) {
        Optional<Ratings> existingRating = ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId,true);
        if(existingRating.isPresent()) {
            Ratings ratings = existingRating.get();
            if(ratings.getMovie().isStatus() && ratings.isStatus()) {
                ratings.setStatus(false);
                ratingsRepository.save(ratings);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You cannot delete rating. Movie or rating is not active.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rating not found for the given movie and user ID");
        }
    }


    @Override
    public List<Ratings> getAllReviewForTheUserID(int userId) {
        return ratingsRepository.findByUserIdAndStatus(userId, true);
    }

    @Override
    public List<Ratings> getAllReviewForTheMovieID(int movieId) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if (optionalMovie.isPresent() && optionalMovie.get().isStatus()) {
            return ratingsRepository.findByMovieIdAndStatus(movieId, true);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot get reviews. Movie is inactive or not found.");
        }
    }
}
