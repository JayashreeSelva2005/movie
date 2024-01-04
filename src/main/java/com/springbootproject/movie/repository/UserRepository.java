package com.springbootproject.movie.repository;

import com.springbootproject.movie.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

   //User findByEmail(String email);
   Optional<User> findByEmail(String email);
   Optional<User> findOneByEmailAndPassword(String email, String encodedPassword);
}
