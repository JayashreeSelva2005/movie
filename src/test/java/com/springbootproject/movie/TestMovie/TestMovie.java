//package com.springbootproject.movie.TestMovie;
//
//import com.springbootproject.movie.Exception.MovieNotFoundException;
//import com.springbootproject.movie.Model.CrewMember;
//import com.springbootproject.movie.Model.Movie;
//import com.springbootproject.movie.Service.MovieServiceImpl;
//import com.springbootproject.movie.repository.MovieRepository;
////import com.springbootproject.movie.service.MovieService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.web.server.ResponseStatusException;
//
////import javax.transaction.Transactional;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//
//@SpringBootTest
//public class TestMovie {
//
//    @Autowired
//    @InjectMocks
//    private MovieServiceImpl movieService;
//
//    @Autowired
//    @Mock
//    private MovieRepository movieRepository;
//
//
//
//
//
//    @Test
//    public void testCreateMovie() {
//        Movie movie = new Movie("anjaan", "nice novie", null, true, null);
//
//        ResponseEntity<Movie> response = movieService.createMovie(movie);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("anjaan", response.getBody().getName());
//
//        movieRepository.delete(response.getBody());
//    }
//
//    @Test
//    @Transactional
//    public void testDeleteMovieById() {
//
//        Movie movie = new Movie("TestMovie", "TestDescription", null, true, null);
//        movieRepository.save(movie);
//
//        ResponseEntity<Void> response = movieService.deleteMovieById(movie.getId());
//        assertEquals(204, response.getStatusCodeValue());
//
//        Movie deletedMovie = movieRepository.findById(movie.getId()).orElse(null);
//        assertNotNull(deletedMovie);
//        assertFalse(deletedMovie.isStatus());
//    }
//
//    @Test
//    public void testDeleteMovieByIdNonExistent() {
//
//        assertThrows(ResponseStatusException.class, () -> movieService.deleteMovieById(999));
//    }
//
//    @Test
//    @Transactional
//    public void testDeleteMovieByIdInactiveMovie() {
//
//        Movie movie = new Movie("InactiveMovie", "InactiveDescription", null, false, null);
//        movieRepository.save(movie);
//
//        assertThrows(ResponseStatusException.class, () -> movieService.deleteMovieById(movie.getId()));
//    }
//
//}
