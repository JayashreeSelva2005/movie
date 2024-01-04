package com.springbootproject.movie.Service;

import com.springbootproject.movie.Exception.MovieNotFoundException;
import com.springbootproject.movie.Model.CrewMember;
import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.repository.CrewMemberRepository;
import com.springbootproject.movie.repository.MovieRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final CrewMemberRepository crewMemberRepository;


    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, CrewMemberRepository crewMemberRepository) {
        this.movieRepository = movieRepository;
        this.crewMemberRepository = crewMemberRepository;
    }

    @Override
    public List<Movie> findAllMovies() {
        return movieRepository.findAllByStatus(true);
    }


    @Override
    public ResponseEntity<Movie> createMovie(Movie movie) {
        try {

            Optional<Movie> existingMovie = movieRepository.findByName(movie.getName());
            if (existingMovie.isPresent()) {
                Movie movieToCreate = existingMovie.get();
                if (!movieToCreate.isStatus()) {
                    movieToCreate.setStatus(true);
                    Movie savedMovie = movieRepository.save(movieToCreate);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie with the same name already exists and is active.");
                }
            } else {
                movie.setStatus(true);
                Movie savedMovie = movieRepository.save(movie);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create movie");
        }
    }

    @Override
    public Movie retriveMovie(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new MovieNotFoundException("Movie cannot be found");
        }
        Movie movie1 = movie.get();
        if (!movie1.isStatus()) {
            throw new MovieNotFoundException("Movie with id " + id + " is not active.");
        }
        return movie.get();
    }

    @Override
    public List<Movie> findAllMoviesWithCrewDetails() {
        try {
            List<Movie> movies = movieRepository.findAllByStatus(true);
            for (Movie movie : movies) {
                List<CrewMember> crewMembers = crewMemberRepository.findAllByMovieId(movie.getId());
                movie.setCrewMemberList(crewMembers);
            }
            return movies;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot retrieve all the movies");
        }
    }

    @Override
    public Movie retriveMovieWithCrewDetails(int id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isEmpty()) {
            throw new MovieNotFoundException("id:" + id);
        }
        Movie movie = optionalMovie.get();
        if (!movie.isStatus()) {
            throw new MovieNotFoundException("Movie with id " + id + " is not active.");
        }
        List<CrewMember> crewMembers = crewMemberRepository.findAllByMovieId(movie.getId());
        movie.setCrewMemberList(crewMembers);

        return movie;
    }

    @Override
    public ResponseEntity<Movie> createMovieWithCrewDetails(Movie movie) {
        try {
            Optional<Movie> existingMovie = movieRepository.findByName(movie.getName());
            if (existingMovie.isPresent()) {

                Movie movieToCreate= existingMovie.get();

                if (!movieToCreate.isStatus()) {

                    movieToCreate.setStatus(true);
                    Movie savedMovie = movieRepository.save(movieToCreate);
                    List<CrewMember> crewMembers = movie.getCrewMemberList();
                    if (crewMembers != null) {
                        for (CrewMember crewMember : crewMembers) {
                            crewMember.setMovie(savedMovie);
                            crewMemberRepository.save(crewMember);
                        }
                        savedMovie.setCrewMemberList(crewMembers);
                    }
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);

                } else {

                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie with the same name already exists and is active.");

                }
            } else {

                movie.setStatus(true);
                Movie savedMovie = movieRepository.save(movie);

                List<CrewMember> crewMembers = movie.getCrewMemberList();
                if (crewMembers != null) {
                    for (CrewMember crewMember : crewMembers) {
                        crewMember.setMovie(savedMovie);
                        crewMemberRepository.save(crewMember);
                    }
                    savedMovie.setCrewMemberList(crewMembers);
                }

                return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't create movie with crewDetails");
        }
    }



    @Override
    public ResponseEntity<Movie> updateMovieDetailsWithId(int id, Movie updatedMovie) {
        try {
            Movie existingMovie = movieRepository.findById(id)
                    .orElseThrow(() -> new MovieNotFoundException("Cannot find the find a movie:" + id));

            if (!existingMovie.isStatus()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update an inactive movie");
            }

            existingMovie.setName(updatedMovie.getName());
            existingMovie.setDescription(updatedMovie.getDescription());

            Movie savedMovie = movieRepository.save(existingMovie);

            return ResponseEntity.ok(savedMovie);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't update the movie");
        }
    }

    @Override
    public ResponseEntity<Void> deleteMovieById(int id) {
        try {
            Movie existingMovie = movieRepository.findById(id)
                    .orElseThrow(() -> new MovieNotFoundException("Movie is not found with this id:" + id));

            if (!existingMovie.isStatus()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete an inactive movie");
            }

            existingMovie.setStatus(false);
            movieRepository.save(existingMovie);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't update the movie");
        }
    }
// My alternative approach to create movie , to set a new record  instead of updating the existing one to know the record of movie's timeline in the app
//    @Override
//    public ResponseEntity<Movie> createMovie(Movie movie) {
//        try {
//            Optional<Movie> existingMovie = movieRepository.findByName(movie.getName());
//            if (existingMovie.isPresent()) {
//                Movie existingMovieEntity = existingMovie.get();
//                if (!existingMovieEntity.isStatus()) {
//                    Movie newMovie = new Movie();
//                    newMovie.setName(existingMovieEntity.getName());
//                    newMovie.setDescription(existingMovieEntity.getDescription());
//                    newMovie.setStatus(true);
//                    Movie savedMovie = movieRepository.save(newMovie);
//                    return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
//                } else {
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie with the same name already exists and is active.");
//                }
//            } else {
//                // Movie with the same name does not exist, create a new movie
//                movie.setStatus(true);
//                Movie savedMovie = movieRepository.save(movie);
//                return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
//            }
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create movie");
//        }
//    }
//

//    @Override
//    public ResponseEntity<Movie> updateMovieWithCrewDetails(int id, Movie updatedMovie) {
//        try {
//
//            Movie existingMovie = movieRepository.findById(id)
//                    .orElseThrow(() -> new MovieNotFoundException("id:" + id));
//
//            existingMovie.setName(updatedMovie.getName());
//            existingMovie.setDescription(updatedMovie.getDescription());
//
//            List<CrewMember> updatedCrewMembers = updatedMovie.getCrewMemberList();
//            if (updatedCrewMembers != null) {
//                for (CrewMember updatedCrewMember : updatedCrewMembers) {
//                    updatedCrewMember.setMovie(existingMovie);
//                    crewMemberRepository.save(updatedCrewMember);
//                }
//                existingMovie.setCrewMemberList(updatedCrewMembers);
//            }
//            Movie savedMovie = movieRepository.save(existingMovie);
//
//            return ResponseEntity.ok(savedMovie);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
//        }
//    }

//    @Override
//    public ResponseEntity<Movie> createMovie(Movie movie) {
//        try {
//            movie.setStatus(true);
//            Movie savedMovie = movieRepository.save(movie);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create movie");
//        }
//    }

//    @Override
//    public ResponseEntity<Movie> createMovieWithCrewDetails(Movie movie) {
//        try {
//            movie.setStatus(true);
//            Movie savedMovie = movieRepository.save(movie);
//            List<CrewMember> crewMembers = movie.getCrewMemberList();
//            if (crewMembers != null) {
//                for (CrewMember crewMember : crewMembers) {
//                    crewMember.setMovie(savedMovie);
//                    crewMemberRepository.save(crewMember);
//                }
//                savedMovie.setCrewMemberList(crewMembers);
//            }
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't create movie with crewDetails");
//        }
//
//    }
}


