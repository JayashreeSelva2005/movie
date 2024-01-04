package com.springbootproject.movie.Service;

import com.springbootproject.movie.Model.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MovieService {
    List<Movie> findAllMovies();

    ResponseEntity<Movie> createMovie(Movie movie);

    Movie retriveMovie(int id);

    List<Movie> findAllMoviesWithCrewDetails();

    Movie retriveMovieWithCrewDetails(int id);

    ResponseEntity<Movie> createMovieWithCrewDetails(Movie movie);

    ResponseEntity<Movie> updateMovieDetailsWithId(int id, Movie updatedMovie);

    ResponseEntity<Void> deleteMovieById(int id);
}
