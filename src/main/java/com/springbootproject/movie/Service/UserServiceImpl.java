package com.springbootproject.movie.Service;

import com.springbootproject.movie.DTO.LoginDTO;
import com.springbootproject.movie.Exception.UserNotFoundException;
import com.springbootproject.movie.Model.User;
import com.springbootproject.movie.repository.UserRepository;
import com.springbootproject.movie.payload.response.LoginMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<User> createUser(User user) {
        try {
            //this.passwordEncoder.encode(user.getPassword())// later
            User savedUser = (User) userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public User retrieveUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id:" + id);
        }
        return user.get();
    }

    public ResponseEntity<User> updateUser(int id, User updatedUser) {
        try {
            if (userRepository.existsById(id)) {
                updatedUser.setId(id);
                User savedUser = (User) userRepository.save(updatedUser);
                return ResponseEntity.ok(savedUser);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found");
        }
    }




    @Override
    public LoginMessage loginUser(LoginDTO loginDTO) {
        String msg = "";
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());

        if (userOptional.isPresent()) {
            User user1 = userOptional.get();
            String password = loginDTO.getPassword();
            String encodedPassword = user1.getPassword();

            if (password.equals(encodedPassword)) {
                Optional<User> user = userRepository.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);

                if (user.isPresent()) {
                    return new LoginMessage("Login Success", true);
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {
                return new LoginMessage("Password Not Match", false);
            }
        } else {
            return new LoginMessage("Email not exists", false);
        }
    }



//        public ResponseEntity<User> createUser( @RequestBody User user){
//            User savedUser = (User) userRepository.save(user);
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(savedUser.getId()).toUri();
//            return  ResponseEntity.created(location).build();
//        }


}
