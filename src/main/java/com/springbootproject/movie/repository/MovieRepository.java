package com.springbootproject.movie.repository;

import com.springbootproject.movie.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MovieRepository extends JpaRepository<Movie,Integer> {
    @Query("SELECT m FROM Movie m WHERE m.status = :status")
    List<Movie> findAllByStatus(@Param("status") boolean status);

    Optional<Movie> findByName(String name);
}
