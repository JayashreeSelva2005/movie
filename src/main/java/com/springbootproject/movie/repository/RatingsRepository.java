package com.springbootproject.movie.repository;


import com.springbootproject.movie.Model.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings,Integer> {
    Optional<Ratings> findByUserIdAndMovieId(int userId, int movieId);

    List<Ratings> findByUserId(int userId);

    List<Ratings> findByMovieId(int movieId);

    List<Ratings> findByUserIdAndStatus(int userId, boolean status);

    List<Ratings> findByMovieIdAndStatus(int movieId, boolean status);

    Optional<Ratings> findByUserIdAndMovieIdAndStatus(int userId, int movieId, boolean status);
}
