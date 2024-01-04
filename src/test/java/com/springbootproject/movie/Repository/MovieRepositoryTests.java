package com.springbootproject.movie.Repository;

import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.Service.MovieService;
import com.springbootproject.movie.Service.MovieServiceImpl;
import com.springbootproject.movie.repository.MovieRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ExpectedCount;

import java.util.List;
import java.util.Optional;

//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;


@DataJpaTest
@ContextConfiguration(classes = {MovieServiceImpl.class, MovieRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieServiceImpl movieService;

    @Test
    @Rollback(true)
    public void testCreateMovie() {

        Movie movie = new Movie();
        movie.setName("Inception-3");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);

        Movie savedMovie = movieRepository.save(movie);

        assertNotNull(savedMovie);
        assertTrue(savedMovie.getId() > 0);
        Assertions.assertEquals("Inception-3", savedMovie.getName());
        Assertions.assertEquals("A mind-bending movie", savedMovie.getDescription());
        assertTrue(savedMovie.isStatus());
    }


    @Test
    @Rollback(true)
    public void MovieRepository_GetAll_ReturnMoreThanOneMovie() {
        Movie movie1 = new Movie();
        movie1.setName("Inception-4");
        movie1.setDescription("A mind-bending movie");
        movie1.setStatus(true);

        Movie movie2 = new Movie();
        movie2.setName("Interstellar-2");
        movie2.setDescription("A space odyssey");
        movie2.setStatus(true);

        movieRepository.save(movie1);
        movieRepository.save(movie2);

        List<Movie> movieList = movieRepository.findAll();

        assertNotNull(movieList);
        assertTrue(movieList.size() >= 2);
    }

    @Test
    public void testUpdateMovie1() {

        Movie movie = new Movie();
        movie.setName("Inception-36");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);
        Movie savedMovie = movieRepository.save(movie);

        Movie movieToUpdate = movieRepository.findById(savedMovie.getId()).orElse(null);
        assertNotNull(movieToUpdate);
        movieToUpdate.setName("A thriller Inception");
        movieToUpdate.setDescription("An updated mind-bending movie");
        movieToUpdate.setStatus(true);

        ResponseEntity <Movie> responseEntity = movieService.updateMovieDetailsWithId(savedMovie.getId(),movieToUpdate);
        assertNotNull(responseEntity);

        Movie updatedMovie = responseEntity.getBody();
        assertNotNull(updatedMovie);

        Assertions.assertEquals("A thriller Inception", updatedMovie.getName());
        Assertions.assertEquals("An updated mind-bending movie", updatedMovie.getDescription());
        assertFalse(updatedMovie.isStatus());
    }

    @Test
    public void testUpdateMovie() {

        Movie movie = new Movie();
        movie.setName("Inception-3");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);
        Movie savedMovie = movieRepository.save(movie);


        Movie movieToUpdate = new Movie();
        movieToUpdate.setName("A thriller Inception");
        movieToUpdate.setDescription("An updated mind-bending movie");
        movieToUpdate.setStatus(true);


        when(movieService.updateMovieDetailsWithId(Mockito.eq(savedMovie.getId()), Mockito.any(Movie.class)))
                .thenReturn(ResponseEntity.ok(movieToUpdate));


        ResponseEntity<Movie> responseEntity = movieService.updateMovieDetailsWithId(savedMovie.getId(), movieToUpdate);


        assertNotNull(responseEntity);

        Movie updatedMovie = responseEntity.getBody();
        assertNotNull(updatedMovie);

        Assertions.assertEquals("A thriller Inception", updatedMovie.getName());
        Assertions.assertEquals("An updated mind-bending movie", updatedMovie.getDescription());
        assertTrue(updatedMovie.isStatus());
    }

    @Test
    @Rollback(false)
    public void testDeleteMovie1() {
        Movie movie = new Movie();
        movie.setName("Tic Tic Tic  - 6");
        movie.setDescription("A space odyssey");
        movie.setStatus(true);
        Movie savedMovie = movieRepository.save(movie);

        ResponseEntity<Void> response = movieService.deleteMovieById(savedMovie.getId());
        System.out.println("savedMovie.getId()"+savedMovie.getId());
        System.out.println("savedMovie.isStatus()"+savedMovie.isStatus());

        Optional<Movie> deletedMovie = movieRepository.findById(savedMovie.getId());
        assertTrue(deletedMovie.isPresent());
        assertFalse(deletedMovie.get().isStatus());
    }

    @Test
   // @Rollback(false)
    public void testDeleteMovie() {

        Movie movie = new Movie();
        movie.setName("Tic Tic Tic - 176");
        movie.setDescription("A space odyssey");
        movie.setStatus(true);
        Movie savedMovie = movieRepository.save(movie);

        when(movieService.deleteMovieById(savedMovie.getId()))
                .thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = movieService.deleteMovieById(savedMovie.getId());

        assertNotNull(response);

        Optional<Movie> deletedMovie = movieRepository.findById(savedMovie.getId());
        assertTrue(deletedMovie.isPresent());
        assertFalse(deletedMovie.get().isStatus());
    }


    @Test
    public void findById_ShouldReturnMovie() {

        Movie movie = createAndSaveMovie("Tic Tic Tic  - 7");

        Optional<Movie> foundMovie = movieRepository.findById(movie.getId());

        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get().getId()).isEqualTo(movie.getId());
    }

    @Test
    public void findByName_ShouldReturnMovie() {
        Movie movie = createAndSaveMovie("Tic Tic Tic  - 8");

        Optional<Movie> foundMovie = movieRepository.findByName(movie.getName());

        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get().getName()).isEqualTo(movie.getName());
    }

    private Movie createAndSaveMovie(String name) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setDescription("A test movie");
        movie.setStatus(true);

        // Save the movie to the repository
        return movieRepository.save(movie);
    }
}
