package com.springbootproject.movie.Service;

import com.springbootproject.movie.DTO.LoginDTO;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.payload.response.LoginMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    ResponseEntity<User> createUser(User user);

    void deleteUser(int id);

    User retrieveUser(int id);

    ResponseEntity<User> updateUser(int id, User updatedUser);

    LoginMessage loginUser(LoginDTO loginDTO);
}
