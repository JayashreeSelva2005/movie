package com.springbootproject.movie.controller;

import com.springbootproject.movie.Model.CrewMember;
import com.springbootproject.movie.Service.CrewMemberServiceImpl;
import com.springbootproject.movie.Service.MovieServiceImpl;
import com.springbootproject.movie.repository.MovieRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class CrewMemberController {

    private CrewMemberServiceImpl crewMemberService;
    @Autowired
    public CrewMemberController(CrewMemberServiceImpl crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    @GetMapping("/crewMembers/{movieId}")
    public List<CrewMember> getAllCrewMembersByMovieId(@PathVariable int movieId) {
        return  crewMemberService.findAllCrewMembers(movieId);

    }

    @PostMapping("/crewMembers/create/{movieId}")
    public ResponseEntity<CrewMember> createCrewMember(@PathVariable int movieId ,@Valid  @RequestBody CrewMember crewMember) {
        return crewMemberService.createCrewMember(crewMember,movieId);
    }

    @PutMapping("/crewMembers/update/{movieId}")
    public ResponseEntity<List<CrewMember>> updateCrewMembersForMovie(
            @PathVariable int movieId, @Valid @RequestBody List<CrewMember> updatedCrewMembers) {
        return crewMemberService.updateCrewMembersForMovie(movieId, updatedCrewMembers);
    }

    @DeleteMapping("/crewMembers/delete/{movieId}/{crewMemberId}")
    public ResponseEntity<Void> deleteCrewMemberForMovie(
            @PathVariable int movieId, @PathVariable int crewMemberId) {
        return crewMemberService.deleteCrewMemberForMovie(movieId, crewMemberId);
    }

}
