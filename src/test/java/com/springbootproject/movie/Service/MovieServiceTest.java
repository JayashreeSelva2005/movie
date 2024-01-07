package com.springbootproject.movie.Service;
import com.springbootproject.movie.Exception.MovieNotFoundException;
import com.springbootproject.movie.Model.CrewMember;
import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.repository.CrewMemberRepository;
import com.springbootproject.movie.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Lgit ist;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CrewMemberRepository crewMemberRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie testMovie;

    @BeforeEach
    public void setUp() {
        testMovie = new Movie();
        testMovie.setId(1);
        testMovie.setName("test movie");
        testMovie.setDescription("A test movie");
        testMovie.setStatus(true);
    }

    @Test
    public void createMovie_WhenMovieDoesNotExist_ReturnsCreatedMovie() {

        when(movieRepository.findByName("test movie")).thenReturn(Optional.empty());
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieService.createMovie(testMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMovie, response.getBody());
    }

    @Test
    public void createMovie_WhenMovieExistsAndInactive_ReturnsUpdatedMovie() {

        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(1);
        inactiveMovie.setName("test movie");
        inactiveMovie.setDescription("A test movie");
        inactiveMovie.setStatus(false);

        when(movieRepository.findByName("test movie")).thenReturn(Optional.of(inactiveMovie));
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieService.createMovie(testMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMovie, response.getBody());
    }

    @Test
    public void createMovie_WhenMovieExistsAndActive_ThrowsException() {
        Movie activeMovie = new Movie();
        activeMovie.setId(1);
        activeMovie.setName("test movie");
        activeMovie.setDescription("A test movie");
        activeMovie.setStatus(true);
        when(movieRepository.findByName("test movie")).thenReturn(Optional.of(activeMovie));
        assertThrows(ResponseStatusException.class, () -> {
            movieService.createMovie(testMovie);
        });
    }

    @Test
    public void retrieveMovie_WhenMovieExistsAndIsActive_ReturnsMovie() {
        int movieId = 1;
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(testMovie));

        Movie retrievedMovie = movieService.retriveMovie(movieId);

        assertNotNull(retrievedMovie);
        assertEquals(testMovie, retrievedMovie);
    }

    @Test
    public void retrieveMovie_WhenMovieExistsAndIsInactive_ThrowsException() {
        int movieId = 1;
        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(movieId);
        inactiveMovie.setName("test movie");
        inactiveMovie.setDescription("A test movie");
        inactiveMovie.setStatus(false);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(inactiveMovie));

        assertThrows(MovieNotFoundException.class, () -> {
            movieService.retriveMovie(movieId);
        });
    }

    @Test
    public void retrieveMovie_WhenMovieDoesNotExist_ThrowsException() {
        int nonExistentMovieId = 2;
        when(movieRepository.findById(nonExistentMovieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> {
            movieService.retriveMovie(nonExistentMovieId);
        });
    }

    @Test
    public void findAllMoviesWithCrewDetails_WhenMoviesExist_ReturnsMoviesWithCrewDetails() {

        List<Movie> movies = new ArrayList<>();
        movies.add(testMovie);

        List<CrewMember> crewMembers = new ArrayList<>();
        CrewMember crewMember = new CrewMember();
        crewMember.setId(1);
        crewMember.setName("John Doe");
        crewMember.setRole("Actor");
        crewMembers.add(crewMember);

        when(movieRepository.findAllByStatus(true)).thenReturn(movies);
        when(crewMemberRepository.findAllByMovieId(testMovie.getId())).thenReturn(crewMembers);

        List<Movie> result = movieService.findAllMoviesWithCrewDetails();

        assertNotNull(result);
        assertEquals(1, result.size());

        Movie resultMovie = result.get(0);
        assertEquals(testMovie.getId(), resultMovie.getId());
        assertEquals(testMovie.getName(), resultMovie.getName());
        assertEquals(testMovie.getDescription(), resultMovie.getDescription());
        assertEquals(testMovie.isStatus(), resultMovie.isStatus());
        assertEquals(crewMembers, resultMovie.getCrewMemberList());
    }

    @Test
    public void findAllMoviesWithCrewDetails_WhenNoMoviesExist_ReturnsEmptyList() {
        when(movieRepository.findAllByStatus(true)).thenReturn(new ArrayList<>());

        List<Movie> result = movieService.findAllMoviesWithCrewDetails();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    public void createMovieWithCrewDetails_WhenMovieDoesNotExist_ReturnsCreatedMovie() {
        when(movieRepository.findByName("test movie")).thenReturn(Optional.empty());
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieService.createMovieWithCrewDetails(testMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMovie, response.getBody());
        // dont know why we add this , study later
        Mockito.verify(movieRepository, Mockito.times(1)).findByName("test movie");
        Mockito.verify(movieRepository, Mockito.times(1)).save(Mockito.any(Movie.class));
        Mockito.verify(crewMemberRepository, Mockito.never()).save(Mockito.any(CrewMember.class));
    }

    @Test
    public void createMovieWithCrewDetails_WhenMovieExistsAndInactive_ReturnsUpdatedMovie() {
        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(1);
        inactiveMovie.setName("test movie");
        inactiveMovie.setDescription("A test movie");
        inactiveMovie.setStatus(false);

        when(movieRepository.findByName("test movie")).thenReturn(Optional.of(inactiveMovie));
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieService.createMovieWithCrewDetails(testMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMovie, response.getBody());
        // dont know why we add this , study later
        Mockito.verify(movieRepository, Mockito.times(1)).findByName("test movie");
        Mockito.verify(movieRepository, Mockito.times(1)).save(Mockito.any(Movie.class));
        Mockito.verify(crewMemberRepository, Mockito.never()).save(Mockito.any(CrewMember.class));
    }

    @Test
    public void createMovieWithCrewDetails_WhenMovieExistsAndActive_ThrowsException() {
        Movie activeMovie = new Movie();
        activeMovie.setId(1);
        activeMovie.setName("test movie");
        activeMovie.setDescription("A test movie");
        activeMovie.setStatus(true);

        when(movieRepository.findByName("test movie")).thenReturn(Optional.of(activeMovie));

        assertThrows(ResponseStatusException.class, () -> {
            movieService.createMovieWithCrewDetails(testMovie);
        });
        // dont know why we add this , study later
        Mockito.verify(movieRepository, Mockito.times(1)).findByName("test movie");
        Mockito.verify(movieRepository, Mockito.never()).save(Mockito.any(Movie.class));
        Mockito.verify(crewMemberRepository, Mockito.never()).save(Mockito.any(CrewMember.class));
    }

    @Test
    public void createMovieWithCrewDetails_WhenMovieExistsAndInactive_CreatesCrewMembers() {

        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(1);
        inactiveMovie.setName("test movie");
        inactiveMovie.setDescription("A test movie");
        inactiveMovie.setStatus(false);

        when(movieRepository.findByName("test movie")).thenReturn(Optional.of(inactiveMovie));
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(testMovie);

        List<CrewMember> crewMembers = new ArrayList<>();
        CrewMember crewMember1 = new CrewMember();
        crewMember1.setName("Crew Member 1");
        CrewMember crewMember2 = new CrewMember();
        crewMember2.setName("Crew Member 2");
        crewMembers.add(crewMember1);
        crewMembers.add(crewMember2);

        testMovie.setCrewMemberList(crewMembers);

        ResponseEntity<Movie> response = movieService.createMovieWithCrewDetails(testMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMovie, response.getBody());
        // don't know why we add this , study later
        Mockito.verify(movieRepository, Mockito.times(1)).findByName("test movie");
        Mockito.verify(movieRepository, Mockito.times(1)).save(Mockito.any(Movie.class));
        Mockito.verify(crewMemberRepository, Mockito.times(2)).save(Mockito.any(CrewMember.class));
    }

    @Test
    public void updateMovieDetailsWithId_WhenMovieExistsAndIsActive_ReturnsUpdatedMovie() {
        int movieId = 1;
        Movie updatedMovie = new Movie();
        updatedMovie.setName("Updated Test Movie");
        updatedMovie.setDescription("Updated Test Description");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(testMovie));
        when(movieRepository.save(testMovie)).thenReturn(updatedMovie);

        ResponseEntity<Movie> response = movieService.updateMovieDetailsWithId(movieId, updatedMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedMovie, response.getBody());
    }

    @Test
    public void updateMovieDetailsWithId_WhenMovieExistsAndIsInactive_ThrowsException() {
        int movieId = 1;
        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(movieId);
        inactiveMovie.setName("test movie");
        inactiveMovie.setDescription("A test movie");
        inactiveMovie.setStatus(false);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(inactiveMovie));

        assertThrows(ResponseStatusException.class, () -> {
            movieService.updateMovieDetailsWithId(movieId, inactiveMovie);
        });
    }

    @Test
    public void deleteMovieById_WhenMovieExistsAndIsActive_ReturnsNoContent() {
        int movieId = 1;

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(testMovie));

        ResponseEntity<Void> response = movieService.deleteMovieById(movieId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deleteMovieById_WhenMovieExistsAndIsInactive_ThrowsException() {
        int movieId = 1;
        Movie inactiveMovie = new Movie();
        inactiveMovie.setId(movieId);
        inactiveMovie.setName("test movie");
        inactiveMovie.setDescription("A test movie");
        inactiveMovie.setStatus(false);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(inactiveMovie));

        assertThrows(ResponseStatusException.class, () -> {
            movieService.deleteMovieById(movieId);
        });
    }

    @Test
    public void deleteMovieById_WhenMovieDoesNotExist_ThrowsException() {
        int nonExistentMovieId = 2;
        when(movieRepository.findById(nonExistentMovieId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            movieService.deleteMovieById(nonExistentMovieId);
        });
    }



}
