package com.springbootproject.movie.controller;

import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.Service.MovieServiceImpl;
import com.springbootproject.movie.Service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
public class MovieController {

    private MovieServiceImpl movieService;

    @Autowired
    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public List<Movie> findAllMovies() {
        return movieService.findAllMovies();
    }

    @PostMapping("/movies/create")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @GetMapping("/movies/crewDetails")
    public List<Movie> findAllMoviesWithCrewDetails() {
        return movieService.findAllMoviesWithCrewDetails();
    }


    @GetMapping("/movies/{id}")
    public Movie retriveMovie(@PathVariable int id){
        return movieService.retriveMovie(id);
    }

    @GetMapping("/movies/crewDetails/{id}")
    public Movie retriveMovieWithCrewDetails(@PathVariable int id){
        return movieService.retriveMovieWithCrewDetails(id);
    }

    @PostMapping("/movies/createWithCrewDetails")
    public ResponseEntity<Movie> createMovieWithCrewDetails(@Valid @RequestBody Movie movie) {
        return movieService.createMovieWithCrewDetails(movie);
    }

//    @PutMapping("/movies/updateWithCrewDetails/{id}")
//    public ResponseEntity<Movie> updateMovieWithCrewDetails(@PathVariable int id, @Valid @RequestBody Movie movie) {
//        return movieService.updateMovieWithCrewDetails(id, movie);
//    }


    @PutMapping("/movies/updateDetails/{id}")
    public ResponseEntity<Movie> updateMovieDetailsWithId(@PathVariable int id, @Valid @RequestBody Movie movie) {
        return movieService.updateMovieDetailsWithId(id, movie);
    }

    @DeleteMapping("/movies/delete/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable int id) {
        return movieService.deleteMovieById(id);
    }



}
