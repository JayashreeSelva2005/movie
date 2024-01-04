package com.springbootproject.movie.Service;
import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.Model.Ratings;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.repository.MovieRepository;
import com.springbootproject.movie.repository.RatingsRepository;
import com.springbootproject.movie.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingsServiceTest {
    @Mock
    private RatingsRepository ratingsRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    public void createRatingWithMovieIdAndUserId_WhenRatingDoesNotExist_ReturnsCreatedRating() {
        int movieId = 1;
        int userId = 1;
        Ratings newRating = new Ratings();

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.empty());

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setName("test movie");
        movie.setDescription("test description");
        movie.setStatus(true);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Ratings createdRating = new Ratings();
        createdRating.setId(1);
        createdRating.setScore(4);
        createdRating.setStatus(true);

        when(ratingsRepository.save(newRating)).thenReturn(createdRating);

        Ratings result = ratingService.createRatingWithMovieIdAndUserId(movieId, userId, newRating);

        assertNotNull(result);
        assertEquals(createdRating, result);
    }

    @Test
    public void createRatingWithMovieIdAndUserId_WhenRatingExistsAndIsActive_ThrowsException() {
        int movieId = 1;
        int userId = 1;
        Ratings existingRating = new Ratings();
        existingRating.setStatus(true);

        //        It leads ton an exception called stubbing  , it occurs when the mockito are not used for cleaner code;
        //        Movie movie = new Movie();
        //        movie.setId(movieId);
        //        movie.setName("test movie");
        //        movie.setDescription("test description");
        //        movie.setStatus(true);
        //        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        //
        //        User user = new User();
        //        user.setId(userId);
        //        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.of(existingRating));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingService.createRatingWithMovieIdAndUserId(movieId, userId, new Ratings());
        });

        String expectedMessage = "You have already reviewed the movie";
        assertTrue(exception.getMessage().contains(expectedMessage));

    }

    @Test
    public void createRatingWithMovieIdAndUserId_WhenMovieDoesNotExist_ThrowsException() {
        int movieId = 1;
        int userId = 1;

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.empty());
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            ratingService.createRatingWithMovieIdAndUserId(movieId, userId, new Ratings());
        });

    }

    @Test
    public void createRatingWithMovieIdAndUserId_WhenUserDoesNotExist_ThrowsException() {
        int movieId = 1;
        int userId = 1;

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.empty());

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setStatus(true);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            ratingService.createRatingWithMovieIdAndUserId(movieId, userId, new Ratings());
        });

    }

    @Test
    public void createRatingWithMovieIdAndUserId_WhenMovieIsInactive_ThrowsException() {
        int movieId = 1;
        int userId = 1;
        Ratings newRating = new Ratings();

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.empty());

        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(movieId);
        inactiveMovie.setStatus(false);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(inactiveMovie));

        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingService.createRatingWithMovieIdAndUserId(movieId, userId, newRating);
        });

        String expectedMessage = "You cannot update an inactive movie";
        assertTrue(exception.getMessage().contains(expectedMessage));
        //I don't know how to equalise both the messages
        //assertEquals(exception.getMessage(),expectedMessage);
    }@Test
    public void updateTheRatingWithMovieAndUserId_WhenRatingExistsAndIsActive_ReturnsUpdatedRating() {
        int movieId = 1;
        int userId = 1;

        Ratings updatedRating = new Ratings();
        updatedRating.setScore(5);
        updatedRating.setDescription("Updated review");

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setStatus(true);

        Ratings existingRating = new Ratings();
        existingRating.setId(1);
        existingRating.setScore(4);
        existingRating.setMovie(movie);
        existingRating.setDescription("Original review");
        existingRating.setStatus(true);

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.of(existingRating));
        when(ratingsRepository.save(existingRating)).thenReturn(updatedRating);

        Ratings result = ratingService.updateTheRatingWithMovieAndUserId(movieId, userId, updatedRating);

        assertNotNull(result);
        assertEquals(updatedRating, result);
    }

    @Test
    public void updateTheRatingWithMovieAndUserId_WhenRatingExistsAndIsInactive_ThrowsException() {
        int movieId = 1;
        int userId = 1;
        Ratings updatedRating = new Ratings();
        updatedRating.setScore(5);
        updatedRating.setDescription("Updated review");

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setStatus(true);

        Ratings existingRating = new Ratings();
        existingRating.setId(1);
        existingRating.setScore(4);
        existingRating.setMovie(movie);
        existingRating.setDescription("Original review");
        existingRating.setStatus(false);

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.of(existingRating));

        assertThrows(ResponseStatusException.class, () -> {
            ratingService.updateTheRatingWithMovieAndUserId(movieId, userId, updatedRating);
        });
    }

    @Test
    public void updateTheRatingWithMovieAndUserId_WhenRatingDoesNotExist_ThrowsException() {
        int movieId = 1;
        int userId = 1;
        Ratings updatedRating = new Ratings();
        updatedRating.setScore(5);
        updatedRating.setDescription("Updated review");
        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            ratingService.updateTheRatingWithMovieAndUserId(movieId, userId, updatedRating);
        });
    }

    @Test
    public void deleteTheRatingWithMovieAndUserId_WhenRatingExistsAndIsActive_DeletesRating() {
        int movieId = 1;
        int userId = 1;

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setStatus(true);

        Ratings existingRating = new Ratings();
        existingRating.setId(1);
        existingRating.setMovie(movie);
        existingRating.setStatus(true);

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.of(existingRating));

        ratingService.deleteTheRatingWithMovieAndUserId(movieId, userId);

        assertFalse(existingRating.isStatus());
    }

    @Test
    public void deleteTheRatingWithMovieAndUserId_WhenRatingExistsAndIsInactive_ThrowsException() {
        int movieId = 1;
        int userId = 1;

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setStatus(true);

        Ratings existingRating = new Ratings();
        existingRating.setId(1);
        existingRating.setMovie(movie);
        existingRating.setStatus(false);

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.of(existingRating));

        assertThrows(ResponseStatusException.class, () -> {
            ratingService.deleteTheRatingWithMovieAndUserId(movieId, userId);
        });
    }

    @Test
    public void deleteTheRatingWithMovieAndUserId_WhenRatingDoesNotExist_ThrowsException() {
        int movieId = 1;
        int userId = 1;

        when(ratingsRepository.findByUserIdAndMovieIdAndStatus(userId, movieId, true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingService.deleteTheRatingWithMovieAndUserId(movieId, userId);
        });

        String expectedMessage = "Rating not found for the given movie and user ID";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void getAllReviewForTheMovieID_WhenReviewsExistAndMovieIsActive_ReturnsReviews() {
        int movieId = 1;

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setStatus(true);

        Ratings review = new Ratings();
        review.setId(1);
        review.setScore(4);
        review.setStatus(true);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(ratingsRepository.findByMovieIdAndStatus(movieId, true)).thenReturn(Collections.singletonList(review));

        List<Ratings> result = ratingService.getAllReviewForTheMovieID(movieId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(review, result.get(0));
    }

    @Test
    public void getAllReviewForTheMovieID_WhenMovieIsInactive_ThrowsException() {
        int movieId = 1;

        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(movieId);
        inactiveMovie.setStatus(false);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(inactiveMovie));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ratingService.getAllReviewForTheMovieID(movieId);
        });

        String expectedMessage = "Cannot get reviews. Movie is inactive or not found.";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void getAllReviewForTheMovieID_WhenNoReviewsExist_ReturnsEmptyList() {
        int movieId = 1;

        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setStatus(true);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(ratingsRepository.findByMovieIdAndStatus(movieId, true)).thenReturn(Collections.emptyList());

        List<Ratings> result = ratingService.getAllReviewForTheMovieID(movieId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


}
