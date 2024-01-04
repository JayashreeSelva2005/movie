package com.springbootproject.movie.Service;

import com.springbootproject.movie.Model.CrewMember;
import com.springbootproject.movie.Model.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrewMemberService {
    List<CrewMember> findAllCrewMembers(int movieId);
    ResponseEntity<CrewMember> createCrewMember(CrewMember crewMember , int movieId);
    ResponseEntity<List<CrewMember>> updateCrewMembersForMovie(int movieId, List<CrewMember> updatedCrewMembers);
    ResponseEntity<Void> deleteCrewMemberForMovie(int movieId, int crewMemberId);
}
