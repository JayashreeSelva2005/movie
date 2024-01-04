package com.springbootproject.movie.Service;

import com.springbootproject.movie.Exception.MovieNotFoundException;
import com.springbootproject.movie.Exception.UserNotFoundException;
import com.springbootproject.movie.Model.CrewMember;
import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.repository.CrewMemberRepository;
import com.springbootproject.movie.repository.MovieRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@Service
public class CrewMemberServiceImpl implements CrewMemberService{
    private CrewMemberRepository crewMemberRepository;


    private MovieServiceImpl movieService;

    @Autowired
    public CrewMemberServiceImpl(CrewMemberRepository crewMemberRepository,MovieServiceImpl movieService) {
        this.crewMemberRepository = crewMemberRepository;
        this.movieService =  movieService ;
    }
    @Override
    public List<CrewMember> findAllCrewMembers(int movieId){
        if(crewMemberRepository.existsById(movieId)) {
            return crewMemberRepository.findAllByMovieId(movieId);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CrewMembers not found");
        }
    }

    @Override
    public ResponseEntity<CrewMember> createCrewMember(CrewMember crewMember, int movieId) {
           Optional<Movie> movie = Optional.ofNullable(movieService.retriveMovie(movieId));
                if(movie.isEmpty()){
                    throw new MovieNotFoundException("id:"+movieId);
                }
                crewMember.setMovie(movie.get());
                CrewMember savedCrewMember = crewMemberRepository.save(crewMember);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(savedCrewMember.getId()).toUri();
                return  ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<CrewMember>> updateCrewMembersForMovie(int movieId, List<CrewMember> updatedCrewMembers) {
        try {
            Optional<Movie> movie = Optional.ofNullable(movieService.retriveMovie(movieId));
            if (movie.isEmpty()) {
                throw new MovieNotFoundException("id:" + movieId);
            }
            List<CrewMember> existingCrewMembers = crewMemberRepository.findAllByMovieId(movieId);
            for (CrewMember existingCrewMember : existingCrewMembers) {
                crewMemberRepository.delete(existingCrewMember);
            }

            for (CrewMember updatedCrewMember : updatedCrewMembers) {
                updatedCrewMember.setMovie(movie.get());
                crewMemberRepository.save(updatedCrewMember);
            }

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(movieId).toUri();
            return ResponseEntity.created(location).body(updatedCrewMembers);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @Override
    public ResponseEntity<Void> deleteCrewMemberForMovie(int movieId, int crewMemberId) {
        try {
            Optional<Movie> movie = Optional.ofNullable(movieService.retriveMovie(movieId));
            if (movie.isEmpty()) {
                throw new MovieNotFoundException("id:" + movieId);
            }

            Optional<CrewMember> crewMember = crewMemberRepository.findById(crewMemberId);
            if (crewMember.isEmpty() || crewMember.get().getMovie().getId() != movieId) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Crew member not found for the specified movie");
            }

            crewMemberRepository.deleteById(crewMemberId);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't delete the crew member");
        }
    }

// doubt it is not working
//    public ResponseEntity<CrewMember> createCrewMember(CrewMember crewMember, int movieId) {
//
//        try {
//            Optional<Movie> movie =  movieService.vie;
//            if(movie.isEmpty()){
//                throw new MovieNotFoundException("id:"+movieId);
//            }
//            crewMember.setMovie(movie.get());
//            if(crewMemberRepository.existsById(movieId)){
//
//                CrewMember savedCrewMember = crewMemberRepository.save(crewMember);
//                return ResponseEntity.ok(savedCrewMember);
//            }
//            else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//            }
//        }
//        catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
//        }
//    }

//    @Override
//    public ResponseEntity<List<CrewMember>> updateCrewMembersForMovie(int movieId, List<CrewMember> updatedCrewMembers) {
//        try {
//            Optional<Movie> movie = Optional.ofNullable(movieService.retriveMovie(movieId));
//            if (movie.isEmpty()) {
//                throw new MovieNotFoundException("id:" + movieId);
//            }
//
//            List<CrewMember> existingCrewMembers = crewMemberRepository.findAllByMovieId(movieId);
//            for (CrewMember existingCrewMember : existingCrewMembers) {
//                for (CrewMember updatedCrewMember : updatedCrewMembers) {
//                    if (existingCrewMember.getId() == updatedCrewMember.getId()) {
//
//                        existingCrewMember.setName(updatedCrewMember.getName());
//                        existingCrewMember.setRole(updatedCrewMember.getRole());
//                        crewMemberRepository.save(existingCrewMember);
//
//                        updatedCrewMembers.remove(updatedCrewMember);
//                        break;
//                    }
//                }
//            }
//            for (CrewMember newCrewMember : updatedCrewMembers) {
//                newCrewMember.setMovie(movie.get());
//                crewMemberRepository.save(newCrewMember);
//            }
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(movieId).toUri();
//            return ResponseEntity.created(location).body(existingCrewMembers);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
//        }
//    }


}


