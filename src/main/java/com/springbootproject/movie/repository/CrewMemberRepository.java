package com.springbootproject.movie.repository;

import com.springbootproject.movie.Model.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Integer> {
    List<CrewMember> findAllByMovieId(int movieId);
}
